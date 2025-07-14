package april2nd.board.articleread.service.event.handler;

import april2nd.board.common.event.Event;
import april2nd.board.common.event.payload.EventPayload;

public interface EventHandler<T extends EventPayload> {
    void handle(Event<T> event);
    boolean supports(Event<T> event);
}
