package april2nd.board.view.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ArticleViewCountRepository {
    private final StringRedisTemplate stringRedisTemplate;

    // view::article::{articleId}::view_count
    private static final String KEY_FORMAT = "view::article::%s::view_count";

    public Long read(Long articleId) {
        String result = stringRedisTemplate.opsForValue().get(generateKey(articleId));
        return result != null ? Long.parseLong(result) : 0L;
    }

    public Long increase(Long articleId) {
        Long count = stringRedisTemplate.opsForValue().increment(generateKey(articleId), 1);
        if (count == null) {
            throw new IllegalStateException("Failed to increment view count for article ID: " + articleId);
        }
        return count;
    }

    private String generateKey(Long articleId) {
        return KEY_FORMAT.formatted(articleId);
    }
}
