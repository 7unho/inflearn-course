package april2nd.board.article.service;

import april2nd.board.article.entity.Article;
import april2nd.board.article.entity.BoardArticleCount;
import april2nd.board.article.repository.ArticleRepository;
import april2nd.board.article.repository.BoardArticleCountRepository;
import april2nd.board.article.service.request.ArticleCreateRequest;
import april2nd.board.article.service.request.ArticleUpdateRequest;
import april2nd.board.article.service.response.ArticlePageResponse;
import april2nd.board.article.service.response.ArticleResponse;
import april2nd.board.common.event.EventType;
import april2nd.board.common.event.Snowflake;
import april2nd.board.common.event.payload.article.ArticleCreatedEventPayload;
import april2nd.board.common.event.payload.article.ArticleDeletedEventPayload;
import april2nd.board.common.event.payload.article.ArticleUpdatedEventPayload;
import april2nd.board.common.outboxmessagerelay.OutboxEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final Snowflake snowflake = new Snowflake();
    private final ArticleRepository articleRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;
    private final OutboxEventPublisher outboxEventPublisher;

    @Transactional
    public ArticleResponse create(ArticleCreateRequest request) {
        Article article = articleRepository.save(
                Article.create(
                        snowflake.nextId(),
                        request.getTitle(),
                        request.getContent(),
                        request.getBoardId(),
                        request.getWriterId()
                )
        );

        int result = boardArticleCountRepository.increase(request.getBoardId());
        if (result == 0) {
            boardArticleCountRepository.save(
                    BoardArticleCount.init(request.getBoardId(), 1L)
            );
        }

        outboxEventPublisher.publish(
                EventType.ARTICLE_CREATED,
                ArticleCreatedEventPayload.builder()
                        .articleId(article.getArticleId())
                        .title(article.getTitle())
                        .content(article.getContent())
                        .boardId(article.getBoardId())
                        .writerId(article.getWriterId())
                        .createdAt(article.getCreatedAt())
                        .modifiedAt(article.getModifiedAt())
                        .boardArticleCount(count(article.getBoardId()))
                        .build(),
                article.getBoardId() // shard key
        );

        return ArticleResponse.from(article);
    }

    @Transactional
    public ArticleResponse update(Long articleId, ArticleUpdateRequest request) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        article.update(request.getTitle(), request.getContent());

        outboxEventPublisher.publish(
                EventType.ARTICLE_UPDATED,
                ArticleUpdatedEventPayload.builder()
                        .articleId(article.getArticleId())
                        .title(article.getTitle())
                        .content(article.getContent())
                        .boardId(article.getBoardId())
                        .writerId(article.getWriterId())
                        .createdAt(article.getCreatedAt())
                        .modifiedAt(article.getModifiedAt())
                        .build(),
                article.getBoardId() // shard key
        );

        return ArticleResponse.from(article);
    }

    public ArticleResponse read(Long articleId) {
        return ArticleResponse.from(articleRepository.findById(articleId).orElseThrow());
    }

    @Transactional
    public void delete(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        articleRepository.delete(article);
        boardArticleCountRepository.decrease(article.getBoardId());

        outboxEventPublisher.publish(
                EventType.ARTICLE_DELETED,
                ArticleDeletedEventPayload.builder()
                        .articleId(article.getArticleId())
                        .title(article.getTitle())
                        .content(article.getContent())
                        .boardId(article.getBoardId())
                        .writerId(article.getWriterId())
                        .createdAt(article.getCreatedAt())
                        .modifiedAt(article.getModifiedAt())
                        .boardArticleCount(count(article.getBoardId()))
                        .build(),
                article.getBoardId() // shard key
        );
    }

    public ArticlePageResponse readAll(Long boardId, Long page, Long pageSize) {
        return ArticlePageResponse.of(
                articleRepository.findAll(boardId, (page - 1) * pageSize, pageSize).stream()
                        .map(ArticleResponse::from)
                        .toList(),
                articleRepository.count(
                        boardId,
                        PageLimitCalculator.calculatePageLimit(page, pageSize, 10L)
                )
        );
    }

    public List<ArticleResponse> readAllInfiniteScroll(Long boardId, Long pageSize, Long lastArticleId) {
        List<Article> articles = lastArticleId == null ?
                articleRepository.findAllInfiniteScroll(boardId, pageSize) :
                articleRepository.findAllInfiniteScroll(boardId, pageSize, lastArticleId);

        return articles.stream().map(ArticleResponse::from).toList();
    }

    public Long count(Long boardId) {
        return boardArticleCountRepository.findById(boardId)
                .map(BoardArticleCount::getArticleCount)
                .orElse(0L);
    }
}