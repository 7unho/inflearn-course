package april2nd.board.like.service;

import april2nd.board.common.event.EventType;
import april2nd.board.common.event.Snowflake;
import april2nd.board.common.event.payload.article.ArticleLikedEventPayload;
import april2nd.board.common.event.payload.article.ArticleUnlikedEventPayload;
import april2nd.board.common.outboxmessagerelay.OutboxEventPublisher;
import april2nd.board.like.entity.ArticleLike;
import april2nd.board.like.entity.ArticleLikeCount;
import april2nd.board.like.repository.ArticleLikeCountRepository;
import april2nd.board.like.repository.ArticleLikeRepository;
import april2nd.board.like.service.response.ArticleLikeResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {
    private final Snowflake snowflake = new Snowflake();
    private final OutboxEventPublisher outboxEventPublisher;
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;

    public ArticleLikeResponse read(Long articleId, Long userId) {
        return articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .map(ArticleLikeResponse::from)
                .orElseThrow();
    }

    /**
     * update 구문을 통한 비관적 락
     *
     * @param articleId
     * @param userId
     */
    @Transactional
    public void likePessimisticLock1(Long articleId, Long userId) {
        ArticleLike articleLike = ArticleLike.create(
                snowflake.nextId(),
                articleId,
                userId
        );
        articleLikeRepository.save(articleLike);

        int result = articleLikeCountRepository.increase(articleId);
        if (result == 0) {
            // 최초 요청 시에는 update 하 레코드가 없으므로 1로 초기화
            // 트래픽이 순식간에 몰릴 경우 유실될 수 있으므로, 게시글 생성 시점에 0으로 초기화 해두는 전략을 취할 수 있다.
            articleLikeCountRepository.save(
                    ArticleLikeCount.init(articleId, 1L)
            );
        }

        outboxEventPublisher.publish(
                EventType.ARTICLE_LIKED,
                ArticleLikedEventPayload.builder()
                        .articleLikeId(articleLike.getArticleLikeId())
                        .articleId(articleLike.getArticleId())
                        .userId(articleLike.getUserId())
                        .createdAt(articleLike.getCreatedAt())
                        .articleLikeCount(count(articleLike.getArticleId()))
                        .build(),
                articleLike.getArticleId()
        );
    }

    @Transactional
    public void unlikePessimisticLock1(Long articleId, Long userId) {
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .ifPresent(articleLike -> {
                    articleLikeRepository.delete(articleLike);
                    articleLikeCountRepository.decrease(articleId);

                    outboxEventPublisher.publish(
                            EventType.ARTICLE_UNLIKED,
                            ArticleUnlikedEventPayload.builder()
                                    .articleLikeId(articleLike.getArticleLikeId())
                                    .articleId(articleLike.getArticleId())
                                    .userId(articleLike.getUserId())
                                    .createdAt(articleLike.getCreatedAt())
                                    .articleLikeCount(count(articleLike.getArticleId()))
                                    .build(),
                            articleLike.getArticleId()
                    );
                });
    }

    /**
     * select for update를 통한 비관적 락
     *
     * @param articleId
     * @param userId
     */
    @Transactional
    public void likePessimisticLock2(Long articleId, Long userId) {
        articleLikeRepository.save(
                ArticleLike.create(
                        snowflake.nextId(),
                        articleId,
                        userId
                )
        );

        ArticleLikeCount articleLikeCount = articleLikeCountRepository.findLockedByArticleId(articleId)
                .orElseGet(() -> ArticleLikeCount.init(articleId, 0L));

        articleLikeCount.increase();
        articleLikeCountRepository.save(articleLikeCount); // 최초 생성 시에는 비영속 상태일 수 있으므로 명시적 save 작성
    }

    @Transactional
    public void unlikePessimisticLock2(Long articleId, Long userId) {
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .ifPresent(articleLike -> {
                    articleLikeRepository.delete(articleLike);

                    ArticleLikeCount lockedByArticleId = articleLikeCountRepository.findLockedByArticleId(articleId).orElseThrow();
                    lockedByArticleId.decrease();
                });
    }

    /**
     * 낙관적 락
     *
     * @param articleId
     * @param userId
     */
    @Transactional
    public void likeOptimisticLock(Long articleId, Long userId) {
        articleLikeRepository.save(
                ArticleLike.create(
                        snowflake.nextId(),
                        articleId,
                        userId
                )
        );

        ArticleLikeCount articleLikeCount = articleLikeCountRepository.findById(articleId)
                .orElseGet(() -> ArticleLikeCount.init(articleId, 0L));

        articleLikeCount.increase();
        articleLikeCountRepository.save(articleLikeCount); // 최초 생성 시에는 비영속 상태일 수 있으므로 명시적 save 작성
    }

    @Transactional
    public void unlikeOptimisticLock(Long articleId, Long userId) {
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .ifPresent(articleLike -> {
                    articleLikeRepository.delete(articleLike);
                    ArticleLikeCount lockedByArticleId = articleLikeCountRepository.findById(articleId).orElseThrow();
                    lockedByArticleId.decrease();
                });
    }

    public Long count(Long articleId) {
        return articleLikeCountRepository.findById(articleId)
                .map(ArticleLikeCount::getLikeCount)
                .orElse(0L);
    }
}
