package april2nd.board.hotarticle.service;

import april2nd.board.common.event.Event;
import april2nd.board.common.event.payload.EventPayload;
import april2nd.board.hotarticle.repository.ArticleCreatedTimeRepository;
import april2nd.board.hotarticle.repository.HotArticleListRepository;
import april2nd.board.hotarticle.service.eventhandler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class HotArticleScoreUpdater {
    private final HotArticleListRepository hotArticleListRepository;
    private final HotArticleScoreCalculator hotArticleScoreCalculator;
    private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

    private static final long MAX_HOT_ARTICLE_COUNT = 10;
    private static final Duration HOT_ARTICLE_TTL = Duration.ofDays(10);

    public void update(Event<EventPayload> event, EventHandler<EventPayload> eventHandler) {
        Long articleId = eventHandler.findArticleId(event);
        LocalDateTime createdTime = articleCreatedTimeRepository.read(articleId);

        // 오늘 작성된 게시글이 아니면 업데이트하지 않음
        if (!isArticleCreatedToday(createdTime)) {
            return;
        }

        eventHandler.handle(event);

        long score = hotArticleScoreCalculator.calculate(articleId);
        hotArticleListRepository.add(articleId, createdTime, score, MAX_HOT_ARTICLE_COUNT, HOT_ARTICLE_TTL);
    }

    private boolean isArticleCreatedToday(LocalDateTime createdTime) {
        return createdTime != null &&
               createdTime.toLocalDate().equals(LocalDate.now());
    }
}
