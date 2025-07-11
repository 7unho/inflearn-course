package april2nd.board.comment.service;

import april2nd.board.comment.entity.ArticleCommentCount;
import april2nd.board.comment.entity.CommentPath;
import april2nd.board.comment.entity.CommentV2;
import april2nd.board.comment.repository.ArticleCommentCountRepository;
import april2nd.board.comment.repository.CommentRepositoryV2;
import april2nd.board.comment.service.request.CommentCreateRequestV2;
import april2nd.board.comment.service.response.CommentPageResponse;
import april2nd.board.comment.service.response.CommentResponse;
import april2nd.board.common.event.EventType;
import april2nd.board.common.event.Snowflake;
import april2nd.board.common.event.payload.comment.CommentCreatedEventPayload;
import april2nd.board.common.event.payload.comment.CommentDeletedEventPayload;
import april2nd.board.common.outboxmessagerelay.OutboxEventPublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class CommentServiceV2 {
    private final Snowflake snowflake = new Snowflake();
    private final OutboxEventPublisher outboxEventPublisher;
    private final CommentRepositoryV2 commentRepository;
    private final ArticleCommentCountRepository articleCommentCountRepository;

    @Transactional
    public CommentResponse create(CommentCreateRequestV2 request) {
        CommentV2 parent = findParent(request);

        CommentPath parentCommentPath = parent == null ? CommentPath.create("") : parent.getCommentPath();
        CommentV2 comment = commentRepository.save(
                CommentV2.create(
                        snowflake.nextId(),
                        request.getContent(),
                        request.getArticleId(),
                        request.getWriterId(),
                        parentCommentPath.createChildCommentPath(
                                commentRepository.findDescendantsTopPath(
                                        request.getArticleId(), parentCommentPath.getPath()
                                ).orElse(null)
                        ))
        );

        int result = articleCommentCountRepository.increase(request.getArticleId());
        if (result == 0) {
            articleCommentCountRepository.save(
                    ArticleCommentCount.init(request.getArticleId(), 1L)
            );
        }

        outboxEventPublisher.publish(
                EventType.COMMENT_CREATED,
                CommentCreatedEventPayload.builder()
                        .commentId(comment.getCommentId())
                        .content(comment.getContent())
                        .articleId(comment.getArticleId())
                        .writerId(comment.getWriterId())
                        .deleted(comment.getDeleted())
                        .createdAt(comment.getCreateAt())
                        .articleCommentCount(count(comment.getArticleId()))
                        .build(),
                comment.getArticleId()
        );

        return CommentResponse.from(comment);
    }

    private CommentV2 findParent(CommentCreateRequestV2 request) {
        String parentPath = request.getParentPath();
        if (parentPath == null) return null;

        return commentRepository.findByPath(parentPath)
                .filter(not(CommentV2::getDeleted))
                .orElseThrow();
    }

    public CommentResponse read(Long commentId) {
        return CommentResponse.from(
                commentRepository.findById(commentId).orElseThrow()
        );
    }

    @Transactional
    public void delete(Long commentId) {
        commentRepository.findById(commentId)
                .filter(not(CommentV2::getDeleted))
                .ifPresent(comment -> {
                    // 댓글이 자식 댓글을 가지고 있다면, 삭제하지 않고 논리 삭제
                    if (hasChildren(comment)) {
                        comment.delete();
                    } else {
                        delete(comment);
                    }

                    outboxEventPublisher.publish(
                            EventType.COMMENT_DELETED,
                            CommentDeletedEventPayload.builder()
                                    .commentId(comment.getCommentId())
                                    .content(comment.getContent())
                                    .articleId(comment.getArticleId())
                                    .writerId(comment.getWriterId())
                                    .deleted(comment.getDeleted())
                                    .createdAt(comment.getCreateAt())
                                    .articleCommentCount(count(comment.getArticleId()))
                                    .build(),
                            comment.getArticleId()
                    );
                });
    }

    private boolean hasChildren(CommentV2 comment) {
        return commentRepository.findDescendantsTopPath(
                comment.getArticleId(),
                comment.getCommentPath().getPath()
        ).isPresent();
    }

    public void delete(CommentV2 comment) {
        commentRepository.delete(comment);

        // 댓글 삭제 시, 해당 댓글이 속한 게시글의 댓글 수를 감소시킨다.
        articleCommentCountRepository.decrease(comment.getArticleId());
        if (!comment.isRoot()) {
            commentRepository.findByPath(comment.getCommentPath().getParentPath())
                    .filter(CommentV2::getDeleted)
                    .filter(not(this::hasChildren))
                    .ifPresent(this::delete);
        }
    }

    public CommentPageResponse readAll(Long articleId, Long page, Long pageSize) {
        return CommentPageResponse.of(
                commentRepository.findAll(articleId, (page - 1) * pageSize, pageSize).stream()
                        .map(CommentResponse::from)
                        .toList(),
                commentRepository.count(articleId, PageLimitCalculator.calculatePageLimit(page, pageSize, 10L))
        );
    }

    public List<CommentResponse> readAllInfiniteScroll(Long articleId, String lastPath, Long pageSize) {
        List<CommentV2> comments = lastPath == null ?
                commentRepository.findAllInfiniteScroll(articleId, pageSize) :
                commentRepository.findAllInfiniteScroll(articleId, lastPath, pageSize);

        return comments.stream()
                .map(CommentResponse::from)
                .toList();
    }

    public Long count(Long articleId) {
        return articleCommentCountRepository.findById(articleId)
                .map(ArticleCommentCount::getCommentCount)
                .orElse(0L);
    }
}
