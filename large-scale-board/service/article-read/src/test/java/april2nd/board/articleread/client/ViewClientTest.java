package april2nd.board.articleread.client;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class ViewClientTest {
    @SpyBean
    ViewClient viewClient;

    @Test
    void count_should_use_cache() throws InterruptedException {
        // when
        // 캐시 미스
        viewClient.count(1L);
        // 캐시 히트
        viewClient.count(1L);
        viewClient.count(1L);

        // 캐시 만료까지 대기
        TimeUnit.SECONDS.sleep(3);

        // 캐시 만료 후 재요청
        viewClient.count(1L);

        // then
        verify(viewClient, times(2)).count(1L);
    }
}