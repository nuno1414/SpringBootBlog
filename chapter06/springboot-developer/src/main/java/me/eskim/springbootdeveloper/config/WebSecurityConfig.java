//package me.eskim.springbootdeveloper.config;
//
//import lombok.RequiredArgsConstructor;
//import me.eskim.springbootdeveloper.service.UserDetailService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
//
//// Spring에서 Bean을 수동으로 등록하기 위해서는, 설정 class 위에 @Configuration을 추가하고
//// @Bean을 사용해 수동으로 빈을 등할 수 있다. 이때 메서드의 이름으로 빈의 이름이 결정된다.(빈 이름이 중복되지 않도록 조심)
//// @Configuration을 사용하면 빈을 싱글톤이 되도록 보장해줌
//@Configuration
//@RequiredArgsConstructor
//public class WebSecurityConfig {
//
//    private final UserDetailService userService;
//
//    // 1. 스프링 시큐리티 기능 비활성화 -> 특정영역에 대한 스프링 시큐리티 기능 비활성화(js, css, image, html 등등)
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web) -> web.ignoring()
//                .requestMatchers(toH2Console())
//                .requestMatchers("/static/**");
//    }
//
//    // 2. 특정 HTTP 요청에 대한 웹 기반 보안 구성
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeRequests() // 3. 인증/인가 설정
//                    // requestMatchers : 특정 요청과 일치하는 url에 대한 엑세스를 설정함.
//                    // permitAll : 누구나 접근이 가능하게 설정. 즉 /login", "/signup", "/user"로 요청이 오며 인증/인가 없이도 접근할 수 있음
//                    .requestMatchers("/login", "/signup", "/user").permitAll()
//                    // anyRequest : 위에서 설정한 url 이외의 요청에 대해 설정함
//                    // authenticated : 별도의 인가는 필요하지 않지만 인증이 성공된 상태여야 접근 할 수 있음
//                    .anyRequest().authenticated()
//                .and()
//                .formLogin() // 4. 폼 기반 로그인 설정
//                    // loginPage : 로그인 페이지 경로 설정
//                    .loginPage("/login")
//                    // defaultSuccessUrl : 로그인 완료 시 이동할 경로
//                    .defaultSuccessUrl("/articles")
//                .and()
//                .logout() // 5. 로그아웃 설정
//                    // logoutSuccessUrl : 로그아웃 완료 시 이동할 경로
//                    .logoutSuccessUrl("/login")
//                    // invalidateHttpSession : 로그아웃 이후 세션 전체 삭제 여부 설정
//                    .invalidateHttpSession(true)
//                .and()
//                .csrf().disable() // 6. csrf 비활성화 -> 활성화 권장 => 실습을 원활하게 하기 위해 비활성화함
//                .build();
//    }
//
//    // 7. 인증 관리자 관련 설정
//    @Bean
//    public AuthenticationManager authenticationManager (HttpSecurity http,
//                                                        BCryptPasswordEncoder bCryptPasswordEncoder,
//                                                        UserDetailService userDetailService) throws Exception {
//
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                // userDetailsService : 사용자 정보를 가져올 서비스를 설정.
//                // 이때 설정하는 서비스 클래스는 반드시 UserDetailsService를 상속 받은 클래스여야 함
//                .userDetailsService(userDetailService) // 8. 사용자 정보 서비스 설정
//                // passwordEncoder : 비밀번호를 암호화하기 위한 인코더 설정
//                .passwordEncoder(bCryptPasswordEncoder)
//                .and()
//                .build();
//    }
//
//    // 9. 패스워드 인코더로 사용할 빈 등록
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
