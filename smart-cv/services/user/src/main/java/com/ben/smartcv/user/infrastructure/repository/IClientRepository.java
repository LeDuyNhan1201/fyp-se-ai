package com.ben.smartcv.user.infrastructure.repository;

import java.util.Optional;

import com.ben.smartcv.user.domain.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface IClientRepository extends JpaRepository<Client, String> {

	Optional<Client> findByClientId(String clientId);

}