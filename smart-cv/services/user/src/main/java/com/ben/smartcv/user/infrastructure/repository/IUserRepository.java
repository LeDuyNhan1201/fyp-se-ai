package com.ben.smartcv.user.infrastructure.repository;

import com.ben.smartcv.user.domain.entity.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<SecurityUser, String> {

    Optional<SecurityUser> findByEmail(String email);

}
