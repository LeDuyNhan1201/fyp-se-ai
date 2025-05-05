package com.ben.smartcv.user;

import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.user.domain.model.User;
import com.ben.smartcv.user.infrastructure.repository.IUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SeedDataCommandLineRunner implements CommandLineRunner {

    IUserRepository userRepository;

    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.existsByEmail(Constant.USER_TEST_EMAIL)) return;

        Faker faker = new Faker();
        User user = User.builder()
                .email(Constant.USER_TEST_EMAIL)
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .password(passwordEncoder.encode("123456"))
                .build();
        userRepository.save(user);
    }

}
