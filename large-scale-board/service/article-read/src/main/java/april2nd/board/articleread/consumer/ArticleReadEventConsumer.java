package april2nd.board.articleread.consumer;

import april2nd.board.articleread.service.ArticleReadService;
import april2nd.board.common.event.Event;
import april2nd.board.common.event.EventType;
import april2nd.board.common.event.payload.EventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleReadEventConsumer {
    private final ArticleReadService articleReadService;

    @KafkaListener(topics = {
                    EventType.Topic.APRIL2ND_BOARD_ARTICLE,
                    EventType.Topic.APRIL2ND_BOARD_COMMENT,
                    EventType.Topic.APRIL2ND_BOARD_VIEW,
                    EventType.Topic.APRIL2ND_BOARD_LIKE
    })
    public void listen(String message, Acknowledgment ack) {
        log.info("[ArticleReadEventConsumer.listen] Received message: {}", message);
        Event<EventPayload> event = Event.fromJson(message);
        if (event != null) {
            articleReadService.handleEvent(event);
        }
        ack.acknowledge();
    }
}
