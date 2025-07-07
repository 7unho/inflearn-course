package april2nd.board.view.repository;

import april2nd.board.view.entity.ArticleViewCount;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleViewCountBackUpRepositoryTest {
    @Autowired
    ArticleViewCountBackUpRepository articleViewCountBackUpRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @Transactional
    void updateViewCountTest() {
        // Given
        articleViewCountBackUpRepository.save(
                ArticleViewCount.init(1L, 0L)
        );
        entityManager.flush();
        entityManager.clear();

        // When
        int result1 = articleViewCountBackUpRepository.updateViewCount(1L, 100L);
        int result2 = articleViewCountBackUpRepository.updateViewCount(1L, 300L);
        int result3 = articleViewCountBackUpRepository.updateViewCount(1L, 200L);

        // Then
        assertThat(result1).isEqualTo(1);
        assertThat(result2).isEqualTo(1);
        assertThat(result3).isEqualTo(0);

        ArticleViewCount articleViewCount = articleViewCountBackUpRepository.findById(1L).get();
        assertThat(articleViewCount.getViewCount()).isEqualTo(300L);
    }
}