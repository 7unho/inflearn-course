package april2nd.board.common.event;

import april2nd.board.common.event.payload.EventPayload;
import april2nd.board.common.event.payload.article.*;
import april2nd.board.common.event.payload.comment.CommentCreatedEventPayload;
import april2nd.board.common.event.payload.comment.CommentDeletedEventPayload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {
    ARTICLE_CREATED(ArticleCreatedEventPayload.class, EventType.Topic.APRIL2ND_BOARD_ARTICLE),
    ARTICLE_UPDATED(ArticleUpdatedEventPayload.class, EventType.Topic.APRIL2ND_BOARD_ARTICLE),
    ARTICLE_DELETED(ArticleDeletedEventPayload.class, EventType.Topic.APRIL2ND_BOARD_ARTICLE),
    ARTICLE_LIKED(ArticleLikedEventPayload.class, Topic.APRIL2ND_BOARD_LIKE),
    ARTICLE_UNLIKED(ArticleUnlikedEventPayload.class, Topic.APRIL2ND_BOARD_LIKE),
    ARTICLE_VIEWED(ArticleViewedEventPayload.class, Topic.APRIL2ND_BOARD_VIEW),
    COMMENT_CREATED(CommentCreatedEventPayload.class, EventType.Topic.APRIL2ND_BOARD_COMMENT),
    COMMENT_DELETED(CommentDeletedEventPayload.class, EventType.Topic.APRIL2ND_BOARD_COMMENT),;

    private final Class<? extends EventPayload> payloadClass;
    private final String topic;

    public static EventType from(String eventType) {
        try {
            return valueOf(eventType);
        } catch (Exception e) {
            log.error("[EventType] Invalid event type: {}", eventType, e);
            return null;
        }
    }

    public Class<EventPayload> getPayloadClass() {
        return (Class<EventPayload>) payloadClass;
    }

    public static class Topic {
        public static final String APRIL2ND_BOARD_ARTICLE = "april2nd-board-article";
        public static final String APRIL2ND_BOARD_COMMENT = "april2nd-board-comment";
        public static final String APRIL2ND_BOARD_LIKE = "april2nd-board-like";
        public static final String APRIL2ND_BOARD_VIEW = "april2nd-board-view";
    }
}
