package april2nd.board.like;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "april2nd.board")
@SpringBootApplication
@EnableJpaRepositories(basePackages = "april2nd.board")
public class LikeApplication {
    public static void main(String[] args) {
        SpringApplication.run(LikeApplication.class, args);
    }
}
