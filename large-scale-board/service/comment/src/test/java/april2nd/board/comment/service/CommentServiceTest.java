package april2nd.board.comment.service;

import april2nd.board.comment.entity.Comment;
import april2nd.board.comment.repository.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;

    @Test
    @DisplayName("삭제할 댓글의 자식이 있다면, 삭제 표시만 진행한다.")
    void deleteShouldMarkDeleteIfHasChildren() {
        // given
        Long articleId = 1L;
        Long commentId = 2L;
        Comment comment = createComment(articleId, commentId);
        given(commentRepository.findById(commentId))
                .willReturn(Optional.of(comment));
        given(commentRepository.countBy(articleId, commentId, 2L)).willReturn(2L);

        // when
        commentService.delete(commentId);

        // then
        verify(comment).deleted();
    }

    @Test
    @DisplayName("하위 댓글이 삭제되고, 삭제되지 않은 부모 댓글이라면 하위 댓글만 삭제한다")
    void deleteShouldDeleteChildeOnlyIfNotDeleteParent() {
        // given
        Long articleId = 1L;
        Long commentId = 2L;
        Long parentCommentId = 1L;

        Comment comment = createComment(articleId, commentId, parentCommentId);
        given(comment.isRoot()).willReturn(false);

        Comment parentComment = mock(Comment.class);
        given(parentComment.getDeleted()).willReturn(false);


        given(commentRepository.findById(commentId))
                .willReturn(Optional.of(comment));
        given(commentRepository.countBy(articleId, commentId, 2L)).willReturn(1L);
        given(commentRepository.findById(parentCommentId)).willReturn(Optional.of(parentComment));

        // when
        commentService.delete(commentId);

        // then
        verify(commentRepository).delete(comment);

        // 호출되지 않음을 검증할 때, never를 사용
        verify(commentRepository, never()).delete(parentComment);
    }

    @Test
    @DisplayName("하위 댓글이 삭제되고, 삭제된 부모 댓글이라면 재귀적으로 모두 삭제한다.")
    void deleteShouldDeleteAllRecursivelyIfDeleteParent() {
        // given
        Long articleId = 1L;
        Long commentId = 2L;
        Long parentCommentId = 1L;

        Comment comment = createComment(articleId, commentId, parentCommentId);
        given(comment.isRoot()).willReturn(false);

        // root이며, 삭제된 상태의 parent mock 객체 생성
        Comment parentComment = createComment(articleId, parentCommentId);
        given(parentComment.isRoot()).willReturn(true);
        given(parentComment.getDeleted()).willReturn(true);


        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(commentRepository.countBy(articleId, commentId, 2L)).willReturn(1L);

        given(commentRepository.findById(parentCommentId)).willReturn(Optional.of(parentComment));
        given(commentRepository.countBy(articleId, parentCommentId, 2L)).willReturn(1L);

        // when
        commentService.delete(commentId);

        // then
        verify(commentRepository).delete(comment);
        verify(commentRepository).delete(parentComment);
    }

    private Comment createComment(Long articleId, Long commentId) {
        Comment comment = mock(Comment.class);
        given(comment.getArticleId()).willReturn(articleId);
        given(comment.getCommentId()).willReturn(commentId);

        return comment;
    }

    private Comment createComment(Long articleId, Long commentId, Long parentCommentId) {
        Comment comment = createComment(articleId, commentId);
        given(comment.getParentCommentId()).willReturn(parentCommentId);

        return comment;
    }
}