package april2nd.board.comment.repository;

import april2nd.board.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(
            value = "select count(*) from ( " +
                        "select comment_id from comment " +
                        "where article_id = :articleId and parent_commend_id = :parentCommentId " +
                    ") t " +
                    "limit :limit ",
            nativeQuery = true
    )
    Long countBy(
            @Param("articleId") Long articleId,
            @Param("parentCommentId") Long parentCommentId,
            @Param("limit") Long limit
    );

    @Query(
            value = "SELECT comment.comment_id, comment.content, comment.parent_comment_id, comment.article_id, " +
                    "comment.writer_id, deleted, created_at " +
                    "FROM ( " +
                    "       SELECT comment_id FROM comment WHERE article_id = :articleId " +
                    "       ORDER BY parent_comment_id, comment_id " +
                    "       LIMIT :limit OFFSET :offset " +
                    ") t LEFT JOIN comment on t.comment_id = comment.comment_id ",
            nativeQuery = true
    )
    List<Comment> findAll(
            @Param("articleId") Long articleId,
            @Param("offset") Long offset,
            @Param("limit") Long limit
    );

    @Query(
            value = "SELECT count(*) " +
                    "FROM ( " +
                    "       SELECT comment_id FROM comment WHERE article_id = :articleId LIMIT :limit " +
                    ") t ",
            nativeQuery = true
    )
    Long count(
            @Param("articleId") Long articleId,
            @Param("limit") Long limit
    );

    @Query(
            value = "SELECT comment.comment_id, comment.content, comment.parent_comment_id, comment.article_id, " +
                           "comment.writer_id, deleted, created_at " +
                    "FROM comment " +
                    "WHERE article_id = :articleId " +
                    "ORDER BY parent_comment_id, comment_id " +
                    "LIMIT :limit ",
            nativeQuery = true
    )
    List<Comment> findAllInfiniteScroll(
            @Param("articleId") Long articleId,
            @Param("limit") Long limit
    );

    @Query(
            value = "SELECT comment.comment_id, comment.content, comment.parent_comment_id, comment.article_id, " +
                    "comment.writer_id, deleted, created_at " +
                    "FROM comment " +
                    "WHERE article_id = :articleId " +
                    "AND ( " +
                    "       parent_comment_id > :lastParentCommentId OR" +
                    "       ( parent_comment_id = :lastParentCommentId AND Comment_id > :lastCommentId ) " +
                    ")" +
                    "ORDER BY parent_comment_id, comment_id " +
                    "LIMIT :limit ",
            nativeQuery = true
    )
    List<Comment> findAllInfiniteScroll(
            @Param("articleId") Long articleId,
            @Param("lastParentCommentId") Long lastParentCommentId,
            @Param("lastCommentId") Long lastCommentId,
            @Param("limit") Long limit
    );
}