package com.ben.smartcv.user.application.usecase;

import com.ben.smartcv.user.domain.entity.User;

public interface IUserUseCase {

    User findByEmail(String email);

    User findById(String id);

    boolean existsByEmail(String email);

    void create(User item);

    void seed(int count);

}