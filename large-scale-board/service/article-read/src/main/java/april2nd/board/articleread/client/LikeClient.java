package april2nd.board.articleread.client;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeClient {
    private RestClient restClient;
    @Value("${endpoints.april2nd-board-like-service.url}")
    private String likeServiceUrl;

    @PostConstruct
    public void initRestClient() {
        this.restClient = RestClient.create(likeServiceUrl);
    }

    public long count(Long articleId) {
        try {
            return restClient.get()
                    .uri("/v1/article-likes/articles/{articleId}/count", articleId)
                    .retrieve()
                    .body(Long.class);
        } catch (Exception e) {
            log.error("[LikeClient.count] Failed to comment count with ID: {}", articleId, e);
            return 0;
        }
    }
}
