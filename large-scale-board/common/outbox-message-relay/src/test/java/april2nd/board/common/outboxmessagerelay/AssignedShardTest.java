package april2nd.board.common.outboxmessagerelay;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AssignedShardTest {
    @Test
    @DisplayName("")
    void ofTest() {
        // given
        Long shardCount = 64L;
        List<String> appList = List.of("appId1", "appId2", "appId3");

        // when
        AssignedShard assignedShard1 = AssignedShard.of(appList.get(0), appList, shardCount);
        AssignedShard assignedShard2 = AssignedShard.of(appList.get(1), appList, shardCount);
        AssignedShard assignedShard3 = AssignedShard.of(appList.get(2), appList, shardCount);
        AssignedShard invalidShard = AssignedShard.of("invalid", appList, shardCount);

        List<Long> result = Stream.of(assignedShard1.getShards(), assignedShard2.getShards(), assignedShard3.getShards(), invalidShard.getShards()).flatMap(List::stream).toList();

        // then
        assertThat(result).hasSize(shardCount.intValue());

        for (int i = 0; i < shardCount; i++) {
            assertThat(result.get(i)).isEqualTo(i);
        }

        assertThat(invalidShard.getShards()).isEmpty();
    }
}