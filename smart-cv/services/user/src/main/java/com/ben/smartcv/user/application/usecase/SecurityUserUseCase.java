package com.ben.smartcv.user.application.usecase;

import java.util.Collection;
import java.util.HashSet;

import com.ben.smartcv.user.domain.entity.SecurityUser;
import com.ben.smartcv.user.infrastructure.repository.IUserRepository;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class SecurityUserUseCase implements UserDetailsManager {

	IUserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SecurityUser securityUser = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("ErrorMsg.ResourceNotFound"));

		if (!securityUser.getEmail().equals(username)) {
			throw new UsernameNotFoundException("ErrorMsg.ResourceNotFound");
		}

		Collection<GrantedAuthority> authorities = new HashSet<>();
		securityUser.getAuthorities().forEach(auth ->
				authorities.add(new SimpleGrantedAuthority(auth.getValue())));

		return new User(
				securityUser.getEmail(),
				securityUser.getPassword(),
				securityUser.getEnabled(),
				securityUser.getAccountNonExpired(),
				securityUser.getCredentialsNonExpired(),
				securityUser.getAccountNonLocked(),
				authorities
		);
	}

	@Override
	public boolean userExists(String username) {
		SecurityUser user = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("ErrorMsg.ResourceNotFound"));
        return user.getEmail().equals(username);
    }

	@Override
	public void createUser(UserDetails user) {
	}

	@Override
	public void updateUser(UserDetails user) {
	}

	@Override
	public void deleteUser(String username) {
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
	}

}