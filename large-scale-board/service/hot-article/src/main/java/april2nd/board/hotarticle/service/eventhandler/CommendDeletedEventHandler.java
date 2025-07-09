package april2nd.board.hotarticle.service.eventhandler;

import april2nd.board.common.event.Event;
import april2nd.board.common.event.EventType;
import april2nd.board.common.event.payload.comment.CommentDeletedEventPayload;
import april2nd.board.hotarticle.repository.ArticleCommentCountRepository;
import april2nd.board.hotarticle.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommendDeletedEventHandler implements EventHandler<CommentDeletedEventPayload> {
    private final ArticleCommentCountRepository articleCommentCountRepository;

    @Override
    public void handle(Event<CommentDeletedEventPayload> event) {
        articleCommentCountRepository.createOrUpdate(
                event.getPayload().getArticleId(),
                event.getPayload().getArticleCommentCount(),
                TimeCalculatorUtils.calculateDurationToMidnight()
        );
    }

    @Override
    public boolean supports(Event<CommentDeletedEventPayload> event) {
        return EventType.COMMENT_DELETED == event.getEventType();
    }

    @Override
    public Long findArticleId(Event<CommentDeletedEventPayload> event) {
        return event.getPayload().getArticleId();
    }
}
