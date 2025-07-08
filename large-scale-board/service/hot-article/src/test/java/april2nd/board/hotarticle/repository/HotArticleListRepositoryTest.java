package april2nd.board.hotarticle.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HotArticleListRepositoryTest {
    @Autowired
    HotArticleListRepository hotArticleListRepository;
    @Test
    void addTest() throws InterruptedException {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 7, 8, 0, 0, 0);
        long limit = 3;

        // when
        hotArticleListRepository.add(1L, now, 2L, limit, Duration.ofSeconds(3));
        hotArticleListRepository.add(2L, now, 4L, limit, Duration.ofSeconds(3));
        hotArticleListRepository.add(3L, now, 7L, limit, Duration.ofSeconds(3));
        hotArticleListRepository.add(4L, now, 9L, limit, Duration.ofSeconds(3));
        hotArticleListRepository.add(5L, now, 1L, limit, Duration.ofSeconds(3));

        // then
        List<Long> articleIds = hotArticleListRepository.readAll("20250708");

        assertThat(articleIds).hasSize(Long.valueOf(limit).intValue());
        assertThat(articleIds.get(0)).isEqualTo(4L);
        assertThat(articleIds.get(1)).isEqualTo(3L);
        assertThat(articleIds.get(2)).isEqualTo(2L);

        // ttl을 3초로 설정했으므로, 3초 후에 데이터가 사라져야 함
        TimeUnit.SECONDS.sleep(5);

        assertThat(hotArticleListRepository.readAll("20250708")).isEmpty();
    }
}