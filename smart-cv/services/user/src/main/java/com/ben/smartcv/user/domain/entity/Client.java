package com.ben.smartcv.user.domain.entity;

import com.ben.smartcv.common.domain.AuditingEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "client")
public class Client extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @NonNull
    @Column(name = "client_id", unique = true, nullable = false)
    String clientId;

    @Column(name = "client_id_issued_at")
    Instant clientIdIssuedAt;

    @Column(name = "client_secret")
    String clientSecret;

    @Column(name = "client_secret_expires_at")
    Instant clientSecretExpiresAt;

    @NonNull
    @Column(name = "client_name", nullable = false)
    String clientName;

    @Column(name = "client_authentication_methods", length = 1000)
    String clientAuthenticationMethods;

    @Column(name = "authorization_grant_types", length = 1000)
    String authorizationGrantTypes;

    @Column(name = "redirect_uris", length = 1000)
    String redirectUris;

    @Column(name = "post_logout_redirect_uris", length = 1000)
    String postLogoutRedirectUris;

    @Column(name = "scopes", length = 1000)
    String scopes;

    @Column(name = "client_settings", length = 2000)
    String clientSettings;

    @Column(name = "token_settings", length = 2000)
    String tokenSettings;

}
