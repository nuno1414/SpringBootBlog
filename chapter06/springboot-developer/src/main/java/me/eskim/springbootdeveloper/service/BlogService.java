package me.eskim.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.eskim.springbootdeveloper.domain.Article;
import me.eskim.springbootdeveloper.dto.AddArticleRequest;
import me.eskim.springbootdeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;

@Service // 빈으로 등록
@RequiredArgsConstructor // final이 붙거나 @NotNull 이 붙은 필드의 생성자 추가 -> 생성자 주입 방식
public class BlogService {

    private final BlogRepository blogRepository;

    // 블로그 글 추가 메서드
    public Article save(AddArticleRequest request) {

        return blogRepository.save(request.toEntity());
    }

}
