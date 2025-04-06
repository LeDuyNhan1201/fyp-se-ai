package com.ben.smartcv.user.domain.entity;

import com.ben.smartcv.common.domain.AuditingEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user")
public class SecurityUser extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @NonNull
    @Column(name = "email", unique = true, nullable = false, length = 200)
    String email;

    @NonNull
    @Column(name = "password", nullable = false, length = 500)
    String password;

    @NonNull
    @Column(name = "first_name", nullable = false, length = 100)
    String firstName;

    @NonNull
    @Column(name = "last_name", nullable = false, length = 100)
    String lastName;

    @Column(name = "account_non_expired")
    Boolean accountNonExpired;

    @Column(name = "account_non_locked")
    Boolean accountNonLocked;

    @Column(name = "credentials_non_expired")
    Boolean credentialsNonExpired;

    @Column(name = "enabled")
    Boolean enabled;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "authority_id", referencedColumnName = "id")
            })
    List<Authority> authorities;

}
