package april2nd.board.hotarticle.service.eventhandler;

import april2nd.board.common.event.Event;
import april2nd.board.common.event.EventType;
import april2nd.board.common.event.payload.article.ArticleDeletedEventPayload;
import april2nd.board.hotarticle.repository.ArticleCreatedTimeRepository;
import april2nd.board.hotarticle.repository.HotArticleListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {
    private final HotArticleListRepository hotArticleListRepository;
    private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

    /**
     * 게시글이 삭제되었을 때,
     * 1. 인기글 목록에서 해당 게시글을 제거합니다.
     * 2. 게시글 생성 시간을 저장하는 레포지토리에서 해당 게시글의 생성 시간을 삭제합니다.
     * @param event
     */
    @Override
    public void handle(Event<ArticleDeletedEventPayload> event) {
        ArticleDeletedEventPayload payload = event.getPayload();

        articleCreatedTimeRepository.delete(payload.getArticleId());
        hotArticleListRepository.remove(payload.getArticleId(), payload.getCreatedAt());
    }

    @Override
    public boolean supports(Event<ArticleDeletedEventPayload> event) {
        return EventType.ARTICLE_DELETED == event.getEventType();
    }

    @Override
    public Long findArticleId(Event<ArticleDeletedEventPayload> event) {
        return event.getPayload().getArticleId();
    }
}
