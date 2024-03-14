package me.eskim.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.eskim.springbootdeveloper.domain.Article;
import me.eskim.springbootdeveloper.dto.AddArticleRequest;
import me.eskim.springbootdeveloper.dto.UpdateArticleRequest;
import me.eskim.springbootdeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 빈으로 등록
@RequiredArgsConstructor // final이 붙거나 @NotNull 이 붙은 필드의 생성자 추가 -> 생성자 주입 방식
public class BlogService {

    private final BlogRepository blogRepository;

    // 블로그 글 추가 메서드
    public Article save(AddArticleRequest request) {

        return blogRepository.save(request.toEntity());
    }

    public List<Article> findAll() {

        return blogRepository.findAll();
    }

    public Article findById(Long id) {

        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id)); // id로 조회 시 없으면 예외 발생
    }

    public void delete(Long id) {

        blogRepository.deleteById(id);
    }

    @Transactional // 트랜잭션 메서드 -> 트랜잭션이 보장되어야 하는 부분에서 사용
    public Article update(Long id, UpdateArticleRequest request) {

        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found:" + id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }
}
