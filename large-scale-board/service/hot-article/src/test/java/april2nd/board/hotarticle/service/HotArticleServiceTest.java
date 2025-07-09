package april2nd.board.hotarticle.service;

import april2nd.board.common.event.Event;
import april2nd.board.common.event.EventType;
import april2nd.board.hotarticle.service.eventhandler.EventHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotArticleServiceTest {
    @InjectMocks
    HotArticleService hotArticleService;
    @Mock
    List<EventHandler> eventHandlers;
    @Mock
    HotArticleScoreUpdater hotArticleScoreUpdater;

    @Test
    @DisplayName("이벤트 핸들러가 존재하지 않을 때, 이벤트를 처리하지 않는다.")
    void handleEventIfEventHandleNotFoundTest() {
        // given
        Event event = mock(Event.class);
        EventHandler eventHandler = mock(EventHandler.class);
        given(eventHandler.supports(event)).willReturn(false);
        given(eventHandlers.stream()).willReturn(Stream.of(eventHandler)); // findEventHandler에서 stream을 사용하므로, mock으로 eventHanders가 false를 반환하도록 설정

        // when
        hotArticleService.handleEvent(event);

        // then
        verify(eventHandler, never()).handle(event);
        verify(hotArticleScoreUpdater, never()).update(event, eventHandler);
    }

    @Test
    @DisplayName("게시글 생성 이벤트일 경우, 이벤트 핸들러가 처리하고, 업데이트는 하지 않는다.")
    void handleEventIfArticleCreatedEvent() {
        // given
        Event event = mock(Event.class);
        given(event.getEventType()).willReturn(EventType.ARTICLE_CREATED);

        EventHandler eventHandler = mock(EventHandler.class);
        given(eventHandler.supports(event)).willReturn(true);
        given(eventHandlers.stream()).willReturn(Stream.of(eventHandler));

        // when
        hotArticleService.handleEvent(event);

        // then
        verify(eventHandler).handle(event);
        verify(hotArticleScoreUpdater, never()).update(event, eventHandler); // 게시글 생성 이벤트일 경우, update 메서드는 호출되지 않아야 함
    }

    @Test
    @DisplayName("게시글 삭제 이벤트일 경우, 이벤트 핸들러가 처리하고, 업데이트는 하지 않는다.")
    void handleEventIfArticleDeletedEvent() {
        // given
        Event event = mock(Event.class);
        given(event.getEventType()).willReturn(EventType.ARTICLE_DELETED);

        EventHandler eventHandler = mock(EventHandler.class);
        given(eventHandler.supports(event)).willReturn(true);
        given(eventHandlers.stream()).willReturn(Stream.of(eventHandler));

        // when
        hotArticleService.handleEvent(event);

        // then
        verify(eventHandler).handle(event);
        verify(hotArticleScoreUpdater, never()).update(event, eventHandler); // 게시글 생성 이벤트일 경우, update 메서드는 호출되지 않아야 함
    }

    @Test
    @DisplayName("Score 업데이트 가능한 이벤트일 경우, 이벤트 핸들러가 처리하고, 업데이트를 수행한다.")
    void handleEventIfScoreUpdateableEventTest() {
        // given
        Event event = mock(Event.class);
        given(event.getEventType()).willReturn(mock(EventType.class));

        EventHandler eventHandler = mock(EventHandler.class);
        given(eventHandler.supports(event)).willReturn(true);
        given(eventHandlers.stream()).willReturn(Stream.of(eventHandler));

        // when
        hotArticleService.handleEvent(event);

        // then
        verify(eventHandler, never()).handle(event);
        verify(hotArticleScoreUpdater).update(event, eventHandler); // 게시글 생성 이벤트일 경우, update 메서드는 호출되지 않아야 함
    }
}