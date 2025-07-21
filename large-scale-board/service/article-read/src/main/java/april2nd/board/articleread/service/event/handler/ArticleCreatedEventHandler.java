package april2nd.board.articleread.service.event.handler;

import april2nd.board.articleread.repository.ArticleIdListRepository;
import april2nd.board.articleread.repository.ArticleQueryModel;
import april2nd.board.articleread.repository.ArticleQueryModelRepository;
import april2nd.board.articleread.repository.BoardArticleCountRepository;
import april2nd.board.common.event.Event;
import april2nd.board.common.event.EventType;
import april2nd.board.common.event.payload.article.ArticleCreatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ArticleCreatedEventHandler implements EventHandler<ArticleCreatedEventPayload> {
    private final ArticleIdListRepository articleIdListRepository;
    private final ArticleQueryModelRepository articleQueryModelRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;

    @Override
    public void handle(Event<ArticleCreatedEventPayload> event) {
        ArticleCreatedEventPayload payload = event.getPayload();
        articleQueryModelRepository.create(
                ArticleQueryModel.create(payload),
                Duration.ofDays(1)
        );
        articleIdListRepository.add(payload.getBoardId(), payload.getArticleId(), 1000L);
        boardArticleCountRepository.createOrUpdate(payload.getBoardId(), payload.getBoardArticleCount());
    }

    @Override
    public boolean supports(Event<ArticleCreatedEventPayload> payload) {
        return EventType.ARTICLE_CREATED == payload.getEventType();
    }
}
