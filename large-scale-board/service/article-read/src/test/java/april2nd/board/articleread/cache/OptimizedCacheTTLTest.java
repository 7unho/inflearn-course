package april2nd.board.articleread.cache;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptimizedCacheTTLTest {
    @Test
    void ofTest() {
        // given
        long ttlSeconds = 10L;

        // when
        OptimizedCacheTTL optimizedCacheTTL = OptimizedCacheTTL.of(ttlSeconds);

        // then
        assertNotNull(optimizedCacheTTL);
        assertEquals(ttlSeconds, optimizedCacheTTL.getLogicalTTL().getSeconds());
        assertEquals(ttlSeconds + OptimizedCacheTTL.PHYSICAL_TTL_DELAY_SECONDS, optimizedCacheTTL.getPhysicalTTL().getSeconds());
    }
}