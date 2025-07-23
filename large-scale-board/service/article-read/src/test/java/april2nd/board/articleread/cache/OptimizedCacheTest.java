package april2nd.board.articleread.cache;

import lombok.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class OptimizedCacheTest {
    @Test
    void parseDataTest() {
        parseDataTest("data", 5L);
        parseDataTest(12345, 5L);
        parseDataTest(12345L, 5L);
        parseDataTest(new TestClass("test"), 5L);
    }

    public void parseDataTest(Object data, long ttlSeconds) {
        // given
        OptimizedCache optimizedCache = OptimizedCache.of(data, Duration.ofSeconds(ttlSeconds));

        // when
        Object resolvedData = optimizedCache.parseData(data.getClass());

        // then
        assertThat(resolvedData).isEqualTo(data);
    }

    @Test
    @DisplayName("ttl이 만료되었는지 확인하는 테스트")
    public void isExpiredTest() {
        assertThat(OptimizedCache.of("data", Duration.ofDays(-30)).isExpired()).isTrue();
        assertThat(OptimizedCache.of("data", Duration.ofDays(30)).isExpired()).isFalse();
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestClass {
        private String testData;
    }
}