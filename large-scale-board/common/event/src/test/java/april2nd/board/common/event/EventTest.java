package april2nd.board.common.event;

import april2nd.board.common.event.payload.EventPayload;
import april2nd.board.common.event.payload.article.ArticleCreatedEventPayload;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    @Test
    void serde() {
        // given
        ArticleCreatedEventPayload payload = ArticleCreatedEventPayload.builder()
                .articleId(1L)
                .title("Test Title")
                .content("Test Content")
                .boardId(1L)
                .writerId(1L)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .boardArticleCount(23L)
                .build();

        Event<EventPayload> event = Event.of(
                1234L,
                EventType.ARTICLE_CREATED,
                payload
        );

        String json = event.toJson();

        // when
        Event<EventPayload> result = Event.fromJson(json);
        ArticleCreatedEventPayload resultPayload = (ArticleCreatedEventPayload) result.getPayload();

        // then
        assertThat(result.getEventId()).isEqualTo(1234L);
        assertThat(result.getEventType()).isEqualTo(EventType.ARTICLE_CREATED);
        assertThat(result.getPayload()).isInstanceOf(ArticleCreatedEventPayload.class);

        assertThat(resultPayload.getArticleId()).isEqualTo(payload.getArticleId());
        assertThat(resultPayload.getTitle()).isEqualTo(payload.getTitle());
        assertThat(resultPayload.getCreatedAt()).isEqualTo(payload.getCreatedAt());
    }
}