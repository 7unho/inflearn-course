package april2nd.board.view.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class ArticleViewDistributedLockRepository {
    private final StringRedisTemplate stringRedisTemplate;

    // view::article::{articleId}::user::{userId}::lock
    private static final String KEY_FORMAT = "view::article::%s::user::%s::lock";

    public boolean tryLock(Long articleId, Long userId, Duration ttl) {
        String key = generateKey(articleId, userId);
        return stringRedisTemplate.opsForValue().setIfAbsent(key, "locked", ttl);
    }

    private String generateKey(Long articleId, Long userId) {
        return KEY_FORMAT.formatted(articleId, userId);
    }
}
