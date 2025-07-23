package april2nd.board.articleread.client;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    @Test
    void readCacheableMultiThreadTest() throws InterruptedException {
        // Given
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        viewClient.count(1L); // 초기 캐시 로딩

        // When
        for (int i = 0; i < 5; i++) {
            CountDownLatch latch = new CountDownLatch(5);
            for (int j = 0; j < 5; j++) {
                executorService.submit(() -> {
                    viewClient.count(1L);
                    latch.countDown();
                });
            }
            latch.await();
            TimeUnit.SECONDS.sleep(2);
            System.out.println("======== cache expired ========");
        }
    }
}