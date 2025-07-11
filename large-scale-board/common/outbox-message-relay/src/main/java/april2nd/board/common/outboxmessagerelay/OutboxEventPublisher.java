package april2nd.board.common.outboxmessagerelay;

import april2nd.board.common.event.Event;
import april2nd.board.common.event.EventType;
import april2nd.board.common.event.Snowflake;
import april2nd.board.common.event.payload.EventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxEventPublisher {
    private final Snowflake outboxSnowflake = new Snowflake();
    private final Snowflake eventIdSnowflake = new Snowflake();
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(EventType eventType, EventPayload eventPayload, Long shardKey) {
        Outbox outbox = Outbox.create(
                outboxSnowflake.nextId(),
                eventType,
                Event.of(
                        eventIdSnowflake.nextId(), eventType, eventPayload
                ).toJson(),
                shardKey & MessageRelayConstants.SHARD_COUNT
        );

        applicationEventPublisher.publishEvent(OutboxEvent.of(outbox));
    }
}
