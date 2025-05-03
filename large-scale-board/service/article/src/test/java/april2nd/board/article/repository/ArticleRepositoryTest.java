package april2nd.board.article.repository;

import april2nd.board.article.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepository;
    
    @Test
    void findAllTest() {
        List<Article> articles = articleRepository.findAll(1L, 1499970L, 30L);

        assertNotNull(articles);
        assertTrue(articles.size() > 0);
    }

    @Test
    void countTest() {
        Long count = articleRepository.count(1L, 10000L);

        assertNotNull(count);
        assertThat(count).isEqualTo(10000L);
    }
}