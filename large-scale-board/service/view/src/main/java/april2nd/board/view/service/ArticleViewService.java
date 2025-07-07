package april2nd.board.view.service;

import april2nd.board.view.repository.ArticleViewCountRepository;
import april2nd.board.view.repository.ArticleViewDistributedLockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleViewService {
    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleViewDistributedLockRepository articleViewDistributedLockRepository;
    private final ArticleViewCountBackUpProcessor articleViewCountBackUpProcessor;

    private static final int BACK_UP_BATCH_SIZE = 100; // 백업 트리거가 되는 개수 단위
    private static final Duration TTL = Duration.ofMinutes(10); // 분산 락의 TTL (Time To Live)

    public Long increase(Long articleId, Long userId) {
        // 1. 락 획득 시도, 획득하지 못하면 현재 조회 수 반환
        if (!articleViewDistributedLockRepository.tryLock(articleId, userId, TTL)) {
            log.warn("Failed to acquire lock for article ID: {} and user ID: {}", articleId, userId);
            return articleViewCountRepository.read(articleId);
        }
        log.info("Lock acquired for article ID: {} and user ID: {}", articleId, userId);
        Long count = articleViewCountRepository.increase(articleId);

        if (count % BACK_UP_BATCH_SIZE == 0) {
            articleViewCountBackUpProcessor.backUp(articleId, count);
        }
        return count;
    }

    public Long count(Long articleId) {
        return articleViewCountRepository.read(articleId);
    }
}
