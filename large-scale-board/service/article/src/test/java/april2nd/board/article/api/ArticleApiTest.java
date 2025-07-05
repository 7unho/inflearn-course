package april2nd.board.article.api;

import april2nd.board.article.service.response.ArticlePageResponse;
import april2nd.board.article.service.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ArticleApiTest {
    RestClient restClient = RestClient.create("http://localhost:9000");

    @Test
    void createTest() {
        ArticleResponse response = create(new ArticleCreateRequest(
                "hi",
                "my content",
                1L,
                1L
        ));
        System.out.println("res = " + response);
    }

    ArticleResponse create(ArticleCreateRequest request) {
        return restClient.post()
                .uri("/v1/articles")
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }
    @Test
    void readTest() {
        Long articleId = 166799136342433792L;
        ArticleResponse res = read(articleId);
        System.out.println("res = " + res);
    }

    private ArticleResponse read(Long articleId) {
        return restClient.get()
                .uri("/v1/articles/" + articleId)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void updateTest() {
        Long articleId = 166799136342433792L;
        update(articleId);
        ArticleResponse res = read(articleId);

        assertThat(res.getTitle()).isEqualTo("new title");
        assertThat(res.getContent()).isEqualTo("new content");
    }

    void update(Long articleId) {
        restClient.put()
                .uri("/v1/articles/{articleId}", articleId)
                .body(new ArticleUpdateRequest("new title", "new content"))
                .retrieve();
    }

    @Test
    void deleteTest() {
        Long articleId = 166799136342433792L;
        restClient.delete()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve();
    }

    @Test
    void readAllTest() {
        restClient.get().uri("/v1/articles?boardId=1&pageSize=30&page=50000")
                .retrieve().body(ArticlePageResponse.class);
    }

    @Test
    void readAllInfiniteScrollTest() {
        List<ArticleResponse> firstPage = restClient.get().uri("/v1/articles/infinite-scroll?boardId=1&pageSize=30")
                .retrieve().body(new ParameterizedTypeReference<List<ArticleResponse>>() {
                });

        Long lastArticleId = firstPage.getLast().getArticleId();
        log.info("Last Article Id >>>>>>>>  = " + lastArticleId);

        List<ArticleResponse> secondPage = restClient.get().uri("/v1/articles/infinite-scroll?boardId=1&pageSize=30&lastArticleId={lastArticleId}", lastArticleId)
                .retrieve().body(new ParameterizedTypeReference<List<ArticleResponse>>() {
                });

        secondPage.forEach(article -> {
            log.info(article.toString());
        });
    }

    @Test
    void countTest() {
        ArticleResponse response = create(new ArticleCreateRequest("hi", "my content", 1L, 2L));

        Long beforeDeleted = restClient.get()
                .uri("/v1/articles/{boardId}/count", response.getBoardId())
                .retrieve()
                .body(Long.class);

        restClient.delete()
                .uri("/v1/articles/{articleId}", response.getArticleId())
                .retrieve();

        Long afterDeleted = restClient.get()
                .uri("/v1/articles/{boardId}/count", response.getBoardId())
                .retrieve()
                .body(Long.class);

        assertThat(beforeDeleted).isEqualTo(afterDeleted + 1);
    }

    @Getter
    @AllArgsConstructor
    static class ArticleCreateRequest {
        private String title;
        private String content;
        private Long boardId;
        private Long writerId;
    }

    @Getter
    @AllArgsConstructor
    static class ArticleUpdateRequest {
        private String title;
        private String content;
    }
}
