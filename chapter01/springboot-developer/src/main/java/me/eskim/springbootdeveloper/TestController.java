package me.eskim.springbootdeveloper;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/test")
    public List<Member> getAllMembers() {

        List<Member> memberList = testService.getAllMembers();
        return memberList;
    }
}
