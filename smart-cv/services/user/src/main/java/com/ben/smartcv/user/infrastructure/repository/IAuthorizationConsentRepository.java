package com.ben.smartcv.user.infrastructure.repository;

import java.util.Optional;

import com.ben.smartcv.user.domain.entity.AuthorizationConsent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuthorizationConsentRepository extends JpaRepository<
		AuthorizationConsent,
		AuthorizationConsent.AuthorizationConsentId> {

	Optional<AuthorizationConsent> findByRegisteredClientIdAndPrincipalName(
			String registeredClientId, String principalName);

	void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);

}