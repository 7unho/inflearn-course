package april2nd.board.comment.controller;

import april2nd.board.comment.service.response.CommentPageResponse;
import april2nd.board.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommentApiV2Test {
    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create() {
        CommentResponse response1 = create(new CommentCreateRequestV2(1L, "my comment1", null, 1L));
        CommentResponse response2 = create(new CommentCreateRequestV2(1L, "my comment2", response1.getPath(), 1L));
        CommentResponse response3 = create(new CommentCreateRequestV2(1L, "my comment3", response2.getPath(), 1L));

        System.out.println("response id >> " + response1.getPath());
        System.out.println("response id >> " + response2.getPath());
        System.out.println("response id >> " + response3.getPath());
    }

    CommentResponse create(CommentCreateRequestV2 request) {
        return restClient.post()
                .uri("/v2/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }

    @Test
    void read() {
        restClient.get()
                .uri("/v2/comments/{commentId}", 194666954694598656L)
                .retrieve()
                .body(CommentPageResponse.class);
    }

    @Test
    void readAll() {
        CommentPageResponse response = restClient.get()
                .uri("/v2/comments?articleId=1&pageSize=10&page=1")
                .retrieve()
                .body(CommentPageResponse.class);

        response.getComments().forEach(System.out::println);
    }

    @Test
    void readAll_infiniteScroll() {
        List<CommentResponse> response = restClient.get()
                .uri("/v2/comments/infinite-scroll?articleId=1&pageSize=1&lastPath=00000")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        response.forEach(System.out::println);
    }

    @Test
    void countTest() {
        // give
        CommentResponse response = create(new CommentCreateRequestV2(2L, "my comment1", null, 1L));

        // when
        Long beforeDeleted = restClient.get()
                .uri("/v2/comments/articles/{articleId}/count", response.getArticleId())
                .retrieve()
                .body(Long.class);

        restClient.delete()
                .uri("/v2/comments/{commentId}", response.getCommentId())
                .retrieve();

        Long afterDeleted = restClient.get()
                .uri("/v2/comments/articles/{articleId}/count", response.getArticleId())
                .retrieve()
                .body(Long.class);

        // then
        assertThat(beforeDeleted).isEqualTo(1L);
        assertThat(afterDeleted).isEqualTo(0L);
    }

    @Getter
    @AllArgsConstructor
    public static class CommentCreateRequestV2 {
        private Long articleId;
        private String content;
        private String parentPath;
        private Long writerId;
    }
}