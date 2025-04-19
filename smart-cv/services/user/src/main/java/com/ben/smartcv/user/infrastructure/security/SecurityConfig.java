package com.ben.smartcv.user.infrastructure.security;

import com.ben.smartcv.common.infrastructure.security.JwtAuthenticationEntryPoint;
import com.ben.smartcv.common.infrastructure.security.SecurityProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    SecurityProperties securityProperties;

    CustomJwtDecoder customJwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        String[] staticPublicEndpoints = {
                "/actuator/**",
                "/actuator/health",
                "/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/swagger-resources/**"
        };

        String[] publicEndpoints = Stream.concat(
                Arrays.stream(staticPublicEndpoints),
                securityProperties.getPublicEndpoints().stream()
        ).toArray(String[]::new);

        httpSecurity.authorizeHttpRequests(request -> {
            request.requestMatchers(publicEndpoints)
                    .permitAll()
                    .anyRequest()
                    // .permitAll();
                    .authenticated();

            log.info("Request: {}", request);
        });

        // Configure the JWT decoder and authentication converter
        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(
                                jwtConfigurer -> jwtConfigurer
                                        .decoder(customJwtDecoder)
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

}
