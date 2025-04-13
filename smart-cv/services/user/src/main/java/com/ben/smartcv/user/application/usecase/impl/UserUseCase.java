package com.ben.smartcv.user.application.usecase.impl;

import com.ben.smartcv.common.application.exception.CommonHttpException;
import com.ben.smartcv.user.application.usecase.IUserUseCase;
import com.ben.smartcv.user.domain.entity.User;
import com.ben.smartcv.user.infrastructure.repository.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.ben.smartcv.common.application.exception.CommonError.RESOURCE_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserUseCase implements IUserUseCase {

    IUserRepository userRepository;

    PasswordEncoder passwordEncoder;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new CommonHttpException(RESOURCE_NOT_FOUND, NOT_FOUND, "User " + email));
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new CommonHttpException(RESOURCE_NOT_FOUND, NOT_FOUND, "User"));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void create(User item) {
        userRepository.save(item);
    }

    @Override
    public void seed(int count) {
        Faker faker = new Faker();
        for (int i = 0; i < count; i++) {
            User user = User.builder()
                    .email(faker.internet().emailAddress())
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .password(passwordEncoder.encode("123456"))
                    .build();
            userRepository.save(user);
        }
    }

}