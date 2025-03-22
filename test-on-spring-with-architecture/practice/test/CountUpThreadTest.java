package test;

import main.src.CountUpThread;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;


class CountUpThreadTest {
    @Test
    public void CountUpThread_는_목표치까지_카운트한다_with_sleep() throws Exception {
        // given
        int goal = 100000;
        CountUpThread countUpThread = new CountUpThread(goal);

        // when
        countUpThread.start();
        Thread.sleep(1000);

        // then
        assertThat(countUpThread.getCurrentCount()).isEqualTo(goal);
    }

    @Test
    public void CountUpThread_는_목표치까지_카운트한다_with_join() throws Exception {
        // given
        int goal = 100000;
        CountUpThread countUpThread = new CountUpThread(goal);

        // when
        countUpThread.start();
        countUpThread.join();

        // then
        assertThat(countUpThread.getCurrentCount()).isEqualTo(goal);
    }

    @Test
    public void CountUpThread_는_목표치까지_카운트한다_awaitility() throws Exception {
        // given
        int goal = 100000;
        CountUpThread countUpThread = new CountUpThread(goal);

        // when
        countUpThread.start();
        await()
                .atMost(10, TimeUnit.SECONDS) // 최대 10초까지 기다린다
                .until(countUpThread::getCurrentCount, equalTo(goal)); // count가 목표까지 도달하는지

        // then
        assertThat(countUpThread.getCurrentCount()).isEqualTo(goal);
    }
}