package april2nd.board.articleread.api;

import april2nd.board.articleread.service.response.ArticleReadPageResponse;
import april2nd.board.articleread.service.response.ArticleReadResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class ArticleReadApiTest {
    RestClient articleReadRestClient = RestClient.create("http://localhost:9005");
    RestClient articleRestClient = RestClient.create("http://localhost:9000");

    @Test
    void readTest() {
        ArticleReadResponse response = articleReadRestClient.get()
                .uri("/v1/articles/{articleId}", 174333506028105728L)
                .retrieve()
                .body(ArticleReadResponse.class);

        System.out.println("response = " + response);
    }

    @Test
    void readAllTest() {
        ArticleReadPageResponse response1 = articleReadRestClient.get()
                .uri("/v1/articles?boardId={boardId}&page={page}&pageSize={pageSize}", 1L, 1L, 5L)
                .retrieve()
                .body(ArticleReadPageResponse.class);

        System.out.println("response1.getArticleCount() = " + response1.getArticleCount());
        for (ArticleReadResponse article : response1.getArticles()) {
            System.out.println("article.getArticleId() = " + article.getArticleId());
        }

        ArticleReadPageResponse response2 = articleRestClient.get()
                .uri("/v1/articles?boardId={boardId}&page={page}&pageSize={pageSize}", 1L, 1L, 5L)
                .retrieve()
                .body(ArticleReadPageResponse.class);

        System.out.println("response2.getArticleCount() = " + response2.getArticleCount());
        for (ArticleReadResponse article : response2.getArticles()) {
            System.out.println("article.getArticleId() = " + article.getArticleId());
        }
    }

    @Test
    void readAllInfiniteScrollTest() {
        List<ArticleReadResponse> responses1 = articleReadRestClient.get()
                .uri("/v1/articles/infinite-scroll?boardId={boardId}&pageSize={pageSize}&lastArticleId={lastArticleId}", 1L, 5L, 205703194471038976L)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        for (ArticleReadResponse article : responses1) {
            System.out.println("article.getArticleId() = " + article.getArticleId());
        }

        List<ArticleReadResponse> responses2 = articleRestClient.get()
                .uri("/v1/articles/infinite-scroll?boardId={boardId}&pageSize={pageSize}&lastArticleId={lastArticleId}", 1L, 5L, 205703194471038976L)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        for (ArticleReadResponse article : responses2) {
            System.out.println("article.getArticleId() = " + article.getArticleId());
        }
    }
}
