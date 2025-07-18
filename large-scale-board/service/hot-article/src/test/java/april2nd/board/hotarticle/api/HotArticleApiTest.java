package april2nd.board.hotarticle.api;

import april2nd.board.hotarticle.service.response.HotArticleResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class HotArticleApiTest {
    RestClient restClient = RestClient.create("http://localhost:9004");

    @Test
    void readAllTest() {
        List<HotArticleResponse> responses = restClient.get()
                .uri("/v1/hot-articles/articles/date/{dateStr}", "20250712")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        responses.stream().forEach(System.out::println);
    }
}
