package me.eskim.springbootdeveloper.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller // 컨트롤러라는 것을 명시적으로 표시
public class ExampleController {

    @GetMapping("/thymeleaf/exmaple")
    public String tyhmeleafExample(Model model) { // 뷰로 데이터를 넘겨 주는 모델 객체



        return "";

    }
}

@Getter
@Setter
class Person {

    private Long id;
    private String name;
    private int age;
    private List<String> hobbies;
}
