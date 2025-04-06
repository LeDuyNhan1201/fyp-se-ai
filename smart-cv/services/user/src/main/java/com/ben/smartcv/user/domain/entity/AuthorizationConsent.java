package com.ben.smartcv.user.domain.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@Table(name = "authorization_consent")
@IdClass(AuthorizationConsent.AuthorizationConsentId.class)
public class AuthorizationConsent {

	@Id
	@Column(name = "registered_client_id")
	String registeredClientId;

	@Id
	@Column(name = "principal_name")
	String principalName;

	@Column(name = "authorities", length = 1000)
	String authorities;

	@Getter
	@Setter
	@Builder
	@FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AuthorizationConsentId implements Serializable {
		@Serial
		private static final long serialVersionUID = 1L;

		String registeredClientId;

		String principalName;

        @Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			AuthorizationConsentId that = (AuthorizationConsentId) o;
			return registeredClientId.equals(that.registeredClientId) && principalName.equals(that.principalName);
		}

		@Override
		public int hashCode() {
			return Objects.hash(registeredClientId, principalName);
		}
	}

}