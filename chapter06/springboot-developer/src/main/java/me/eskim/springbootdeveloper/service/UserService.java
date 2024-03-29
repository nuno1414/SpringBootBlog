package me.eskim.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.eskim.springbootdeveloper.domain.User;
import me.eskim.springbootdeveloper.dto.AddUserRequest;
import me.eskim.springbootdeveloper.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto) {

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword())) // 패스워드 암호화
                .build())
                .getId();
    }

}
