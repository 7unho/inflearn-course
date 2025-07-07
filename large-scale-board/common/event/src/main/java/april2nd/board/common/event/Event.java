package april2nd.board.common.event;

import april2nd.board.common.dataderializer.DataSerializer;
import april2nd.board.common.event.payload.EventPayload;
import lombok.Getter;

@Getter
public class Event<T extends EventPayload> {
    private Long eventId;
    private EventType eventType;
    private T payload;

    public static Event<EventPayload> of(Long eventId, EventType eventType, EventPayload eventPayload) {
        Event<EventPayload> event = new Event<>();
        event.eventId = eventId;
        event.eventType = eventType;
        event.payload = eventPayload;
        return event;
    }

    public String toJson() {
        return DataSerializer.serialize(this);
    }

    public static Event<EventPayload> fromJson(String json) {
        EventRaw eventRaw = DataSerializer.deserialize(json, EventRaw.class);
        if (eventRaw == null) {
            return null;
        }
        Event<EventPayload> event = new Event<>();
        event.eventId = eventRaw.getEventId();
        event.eventType = EventType.from(eventRaw.getEventType());
        event.payload = DataSerializer.deserialize(eventRaw.getPayload(), event.eventType.getPayloadClass());
        return event;
    }

    @Getter
    private static class EventRaw {
        private Long eventId;
        private String eventType;
        private Object payload;
    }
}
