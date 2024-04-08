package me.eskim.springbootdeveloper.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.eskim.springbootdeveloper.domain.Article;
import org.hibernate.annotations.BatchSize;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor //모든 필드 값을 파라미터로 받는 생성자 추가
@Getter
public class AddArticleRequest {

    @NotNull
    @Size(min=1, max=10)
    private String title;

    @NotNull
    private  String content;

    public Article toEntity(String author) { // 생성자를 사용해 객체 생성 => dto -> entity 변환

        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}

/**
 * 문자열을 다룰 때 사용
 * @NotNull : null 허용하지 않음
 * @NotEmpty : null, 빈 문자열(공백) 또는 공백만으로 채워진 문자열 허용하지 않음
 * @NotBlank : null, 빈 문자열(공백) 혀용하지 않음
 * @Size(min=?, max=?) : 최소 길이, 최대 길이 제한
 * @Null : null만 가능
 */

/**
 * @Positive : 양수만 허용
 * @PositiveOrZero : 양수와 0만 허용
 * @Negative : 음수만 허용
 * @NegativeOrZero : 음수와 0만 허용
 * @Min(?) : 최솟값 제한
 * @Max(?) : 최댓값 제한
 */

/**
 * 정규식 관련
 * @Email : 이메일 형식만 허용
 * @Pattern(regexp="?") : 직접 작성한 정규식에 맞는 문자열만 허용
 */

