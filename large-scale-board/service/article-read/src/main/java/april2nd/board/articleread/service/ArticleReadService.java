package april2nd.board.articleread.service;

import april2nd.board.articleread.client.ArticleClient;
import april2nd.board.articleread.client.CommentClient;
import april2nd.board.articleread.client.LikeClient;
import april2nd.board.articleread.client.ViewClient;
import april2nd.board.articleread.repository.ArticleQueryModel;
import april2nd.board.articleread.repository.ArticleQueryModelRepository;
import april2nd.board.articleread.service.event.handler.EventHandler;
import april2nd.board.articleread.service.response.ArticleReadResponse;
import april2nd.board.common.event.Event;
import april2nd.board.common.event.payload.EventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleReadService {
    private final ArticleClient articleClient;
    private final CommentClient commentClient;
    private final LikeClient likeClient;
    private final ViewClient viewClient;
    private final ArticleQueryModelRepository articleQueryModelRepository;
    private final List<EventHandler> eventHandlers;

    public void handleEvent(Event<EventPayload> event) {
        for (EventHandler eventHandler : eventHandlers) {
            if (eventHandler.supports(event)) {
                eventHandler.handle(event);
            }
        }
    }

    public ArticleReadResponse read(Long articleId) {
        ArticleQueryModel articleQueryModel = articleQueryModelRepository.read(articleId)
                .or(() -> fetch(articleId))
                .orElseThrow();

        return ArticleReadResponse.from(
                articleQueryModel,
                viewClient.count(articleId)
        );
    }

    private Optional<ArticleQueryModel> fetch(Long articleId) {
        Optional<ArticleQueryModel> articleQueryModelOptional = articleClient.read(articleId)
                .map(article -> ArticleQueryModel.create(
                        article,
                        commentClient.count(articleId),
                        likeClient.count(articleId)
                ));

        articleQueryModelOptional.ifPresent(articleQueryModel -> articleQueryModelRepository.create(articleQueryModel, Duration.ofDays(1)));
        log.info("[ArticleReadService.fetch] fetch data. articleId = {}, isPresent = {}", articleId, articleQueryModelOptional.isPresent());
        return articleQueryModelOptional;
    }
}
