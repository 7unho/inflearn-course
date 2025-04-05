package april2nd.board.article.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Table(name = "article")
@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
    @Id
    private Long articleId;

    private String title;
    private String content;
    private Long boardId;
    private Long writerid;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static Article create(Long articleId, String title, String content, Long boardId, Long writerid, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        Article article = new Article();
        article.articleId = articleId;
        article.title = title;
        article.content = content;
        article.boardId = boardId;
        article.writerid = writerid;
        article.createdAt = createdAt;
        article.modifiedAt = modifiedAt;
        return article;
    }

    public static Article create(Long articleId, String title, String content, Long boardId, Long writerid) {
        Article article = new Article();
        article.articleId = articleId;
        article.title = title;
        article.content = content;
        article.boardId = boardId;
        article.writerid = writerid;
        return article;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        modifiedAt = LocalDateTime.now();
    }
}
