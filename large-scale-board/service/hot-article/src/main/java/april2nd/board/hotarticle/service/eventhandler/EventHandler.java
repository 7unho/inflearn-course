package april2nd.board.hotarticle.service.eventhandler;

import april2nd.board.common.event.Event;
import april2nd.board.common.event.payload.EventPayload;

public interface EventHandler<T extends EventPayload> {
    void handle(Event<T> event);
    boolean supports(Event<T> event);
    Long findArticleId(Event<T> event);
}
