package com.ben.smartcv.user.domain.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "authorization")
public class Authorization {

	@Id
	@Column(name = "id")
	String id;

	@Column(name = "registered_client_id")
	String registeredClientId;

	@Column(name = "principal_name")
	String principalName;

	@Column(name = "authorization_grant_type")
	String authorizationGrantType;

	@Column(name = "authorized_scopes", length = 1000)
	String authorizedScopes;

	@Lob
	@Column(name = "attributes", length = 4000)
	String attributes;

	@Column(name = "state", length = 500)
	String state;

	@Lob
	@Column(name = "authorization_code_value", length = 4000)
	String authorizationCodeValue;

	@Column(name = "authorization_code_issued_at")
	Instant authorizationCodeIssuedAt;

	@Column(name = "authorization_code_expires_at")
	Instant authorizationCodeExpiresAt;

	@Column(name = "authorization_code_metadata")
	String authorizationCodeMetadata;

	@Lob
	@Column(name = "access_token_value", length = 4000)
	String accessTokenValue;

	@Column(name = "access_token_issued_at")
	Instant accessTokenIssuedAt;

	@Column(name = "access_token_expires_at")
	Instant accessTokenExpiresAt;

	@Lob
	@Column(name = "access_token_metadata", length = 2000)
	String accessTokenMetadata;

	@Column(name = "access_token_type")
	String accessTokenType;

	@Column(name = "access_token_scopes", length = 1000)
	String accessTokenScopes;

	@Lob
	@Column(name = "refresh_token_value", length = 4000)
	String refreshTokenValue;

	@Column(name = "refresh_token_issued_at")
	Instant refreshTokenIssuedAt;

	@Column(name = "refresh_token_expires_at")
	Instant refreshTokenExpiresAt;

	@Lob
	@Column(name = "refresh_token_metadata", length = 2000)
	String refreshTokenMetadata;

	@Lob
	@Column(name = "oidc_id_token_value", length = 4000)
	String oidcIdTokenValue;

	@Column(name = "oidc_id_token_issued_at")
	Instant oidcIdTokenIssuedAt;

	@Column(name = "oidc_id_token_expires_at")
	Instant oidcIdTokenExpiresAt;

	@Lob
	@Column(name = "oidc_id_token_metadata", length = 2000)
	String oidcIdTokenMetadata;

	@Lob
	@Column(name = "oidc_id_token_claims", length = 2000)
	String oidcIdTokenClaims;

}