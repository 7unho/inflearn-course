package april2nd.board.view.service;

import april2nd.board.view.repository.ArticleViewCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleViewService {
    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleViewCountBackUpProcessor articleViewCountBackUpProcessor;
    private static final int BACK_UP_BATCH_SIZE = 100; // 백업 트리거가 되는 개수 단위

    public Long increase(Long articleId, Long userId) {
        Long count = articleViewCountRepository.increase(articleId);
        log.info("Article view count increased {} by {}", articleId, userId);
        if (count % BACK_UP_BATCH_SIZE == 0) {
            articleViewCountBackUpProcessor.backUp(articleId, count);
        }
        return count;
    }

    public Long count(Long articleId) {
        return articleViewCountRepository.read(articleId);
    }
}
