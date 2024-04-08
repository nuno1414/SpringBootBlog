package me.eskim.springbootdeveloper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import me.eskim.springbootdeveloper.domain.Article;
import me.eskim.springbootdeveloper.domain.User;
import me.eskim.springbootdeveloper.dto.AddArticleRequest;
import me.eskim.springbootdeveloper.dto.UpdateArticleRequest;
import me.eskim.springbootdeveloper.repository.BlogRepository;
import me.eskim.springbootdeveloper.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 테스트용 어플리케이션 컨텍스트
@AutoConfigureMockMvc // MockMVC 생성 및 자동 구성
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; // 직렬화, 역직렬화를 위한 클래스

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach // 테스트 실행 전 실행하는 메서드
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();

        blogRepository.deleteAll();
    }

    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                        .email("user@gmail.com")
                        .password("test")
                        .build());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
    }

    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        // 객체 JSON으로 직렬화
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // when
        // 설정한 내용을 바탕으로 요청 전송
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1); // 크기가 1인지 검증
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);

    }

    @DisplayName("finaAllArticles: 블로그 글 목록 조회에 성공한다.")
    @Test
    public void findAllArticles() throws Exception {
        // given
        final String url = "/api/articles";
        //final String title = "title";
        //final String content = "content";
        Article savedArticle = createDefaultArticle();

        // 엔티티 객체를 생성하여 저장
        //blogRepository.save(Article.builder()
        //        .title(title)
        //        .content(content)
        //        .build());


        // when
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].content").value(savedArticle.getContent()))
                //.andExpect(jsonPath("$[0].title").value(title));
                .andExpect(jsonPath("$[0].title").value(savedArticle.getTitle()));
    }

    @DisplayName("findArticle: 블로그 글 조회에 성공한다.")
    @Test
    public void findArticle() throws Exception {
        //given
        final String url = "/api/articles/{id}";
        //final String title = "title";
        //final String content = "content";
        Article savedArticle = createDefaultArticle();

        //Article savedArticle = blogRepository.save(Article.builder()
        //        .title(title)
        //        .content(content)
        //        .build());

        // when
        final ResultActions resultActions = mockMvc
                .perform(get(url, savedArticle.getId()));

        // then
        resultActions
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.title").value(savedArticle.getTitle()))
                //.andExpect(jsonPath("$.content").value(content));
                .andExpect(jsonPath("$.content").value(savedArticle.getContent()));
    }

    @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception {

        // given
        final String url = "/api/articles/{id}";
        //final String title = "title";
        //final String content = "content";
        Article savedArticle = createDefaultArticle();

        //Article savedArticle = blogRepository.save(Article.builder()
        //        .title(title)
        //        .content(content)
        //        .build());

        // when
        //final ResultActions resultActions = mockMvc.perform(delete(url, savedArticle.getId()));
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());

        // then
        //resultActions.andExpect(status().isOk());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles).isEmpty();
    }

    @DisplayName("updateArticle: 블로그 글 수정해 성공한다.")
    @Test
    public void updateArticle() throws Exception {

        // given
        final String url = "/api/articles/{id}";
        //final String title = "title";
        //final String content = "content";
        Article savedArticle = createDefaultArticle();

        //Article savedArticle = blogRepository.save(Article.builder()
        //        .title(title)
        //        .content(content)
        //        .build());

        final String newTitle = "new title";
        final String newContent = "new content";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);


        //when
        final ResultActions resultActions = mockMvc
                .perform(put(url, savedArticle.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)));

        // then => resultActions에서 가져온 객체로 판단하는 경우 andExpect 사용
        //         그외 별도로 조회하거나 만든 객체로 판단하는 경우는 assertThat 사용
        resultActions.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();

        /*
        resultActions
                .andExpect(jsonPath("$.title").value(newTitle))
                .andExpect(jsonPath("$.content").value(newContent));
         */

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }

    private Article createDefaultArticle() {

        return blogRepository.save(Article.builder()
                        .title("title")
                        .author(user.getUsername())
                        .content("content")
                        .build());
    }

    @DisplayName("addArticle: 아티클 추가할 때 title이 null이면 실패한다.")
    @Test
    public void addArticleNullValidation() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = null;
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("addArticle: 아티클 추가할 때 title이 10자를 넘으면 실패한다.")
    @Test
    public void addArticleSizeValidation() throws Exception {

        // given
        Faker faker = new Faker();

        final String url = "/api/articles";
        final String title = faker.lorem().characters(11);
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody)
        );

        // then
        resultActions.andExpect(status().isBadRequest());



    }
}