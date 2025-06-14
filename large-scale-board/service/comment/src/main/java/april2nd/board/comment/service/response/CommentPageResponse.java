package april2nd.board.comment.service.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CommentPageResponse {
    private List<CommentResponse> comments;
    private Long commentCount;

    public static CommentPageResponse of(List<CommentResponse> comments, Long commentCount) {
        return CommentPageResponse.builder()
                .comments(comments)
                .commentCount(commentCount)
                .build();
    }
}
