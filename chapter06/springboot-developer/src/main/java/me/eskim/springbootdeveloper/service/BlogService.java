package me.eskim.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.eskim.springbootdeveloper.config.error.exception.ArticleNotFoundException;
import me.eskim.springbootdeveloper.domain.Article;
import me.eskim.springbootdeveloper.dto.AddArticleRequest;
import me.eskim.springbootdeveloper.dto.UpdateArticleRequest;
import me.eskim.springbootdeveloper.repository.BlogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 빈으로 등록
@RequiredArgsConstructor // final이 붙거나 @NotNull 이 붙은 필드의 생성자 추가 -> 생성자 주입 방식
public class BlogService {

    private final BlogRepository blogRepository;

    // 블로그 글 추가 메서드
    public Article save(AddArticleRequest request, String userName) {

        return blogRepository.save(request.toEntity(userName));
    }

    public List<Article> findAll() {

        return blogRepository.findAll();
    }

    public Article findById(Long id) {

        return blogRepository.findById(id)
                //.orElseThrow(() -> new IllegalArgumentException("not found : " + id)); // id로 조회 시 없으면 예외 발생
                    .orElseThrow(ArticleNotFoundException::new); // id로 조회 시 없으면 ArticleNotFoundException 예외 발생
    }

    public void delete(Long id) {

        Article article = blogRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        authorizeArticleAuthor(article);
        blogRepository.deleteById(id);
    }

    @Transactional // 트랜잭션 메서드 -> 트랜잭션이 보장되어야 하는 부분에서 사용
    public Article update(Long id, UpdateArticleRequest request) {

        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found:" + id));

        authorizeArticleAuthor(article);

        article.update(request.getTitle(), request.getContent());

        return article;
    }

    private static void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }
}
