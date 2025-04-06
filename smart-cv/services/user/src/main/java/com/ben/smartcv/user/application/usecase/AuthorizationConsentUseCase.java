package com.ben.smartcv.user.application.usecase;

import java.util.HashSet;
import java.util.Set;
import com.ben.smartcv.user.domain.entity.AuthorizationConsent;
import com.ben.smartcv.user.infrastructure.repository.IAuthorizationConsentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class AuthorizationConsentUseCase implements OAuth2AuthorizationConsentService {

	IAuthorizationConsentRepository authorizationConsentRepository;

	RegisteredClientRepository registeredClientRepository;

	@Transactional
	@Override
	public void save(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
		this.authorizationConsentRepository.save(toEntity(authorizationConsent));
	}

	@Transactional
	@Override
	public void remove(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
		this.authorizationConsentRepository.deleteByRegisteredClientIdAndPrincipalName(
				authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
	}

	@Override
	public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
		Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
		Assert.hasText(principalName, "principalName cannot be empty");
		return this.authorizationConsentRepository.findByRegisteredClientIdAndPrincipalName(
				registeredClientId, principalName).map(this::toObject).orElse(null);
	}

	private OAuth2AuthorizationConsent toObject(AuthorizationConsent authorizationConsent) {
		String registeredClientId = authorizationConsent.getRegisteredClientId();
		RegisteredClient registeredClient = this.registeredClientRepository.findById(registeredClientId);
		if (registeredClient == null) {
			throw new DataRetrievalFailureException(
					"The RegisteredClient with id '" + registeredClientId + "' was not found in database.");
		}

		OAuth2AuthorizationConsent.Builder builder = OAuth2AuthorizationConsent.withId(
				registeredClientId, authorizationConsent.getPrincipalName());

		if (authorizationConsent.getAuthorities() != null) {
			for (String authority : StringUtils.commaDelimitedListToSet(authorizationConsent.getAuthorities())) {
				builder.authority(new SimpleGrantedAuthority(authority));
			}
		}

		return builder.build();
	}

	private AuthorizationConsent toEntity(OAuth2AuthorizationConsent authorizationConsent) {
		AuthorizationConsent entity = new AuthorizationConsent();
		entity.setRegisteredClientId(authorizationConsent.getRegisteredClientId());
		entity.setPrincipalName(authorizationConsent.getPrincipalName());

		Set<String> authorities = new HashSet<>();
		for (GrantedAuthority authority : authorizationConsent.getAuthorities()) {
			authorities.add(authority.getAuthority());
		}
		entity.setAuthorities(StringUtils.collectionToCommaDelimitedString(authorities));
		return entity;
	}

}