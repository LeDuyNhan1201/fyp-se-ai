package com.ben.smartcv.common.util;

//import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.SECONDS;

public final class AuthenticationHelper {

//    public static String extractUserId() {
//        Authentication authentication = getAuthentication();
//
//        if (authentication instanceof AnonymousAuthenticationToken) {
//            throw new Exception("Access denied");
//        }
//
//        JwtAuthenticationToken contextHolder = (JwtAuthenticationToken) authentication;
//
//        return contextHolder.getToken().getSubject();
//    }
//
//    public static String extractJwt() {
//        return ((Jwt) getAuthentication().getPrincipal()).getTokenValue();
//    }
//
    public static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "anonymous";
        }

        String uid = authentication.getName();
        return uid;
    }

    public static String generateToken(String userId) throws JOSEException {
        String signerKey = "Yk3IuXoeV7q+u5ObH78WnQJ+cDcA2lRLU6mkGYYQZjI=";
        JWSHeader jwsHeader = new JWSHeader(Constant.JWT_SIGNATURE_ALGORITHM);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userId)
                .issuer(Constant.JWT_ISSUER)
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1800, SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        jwsObject.sign(new MACSigner(signerKey.getBytes()));
        return jwsObject.serialize();
    }

}