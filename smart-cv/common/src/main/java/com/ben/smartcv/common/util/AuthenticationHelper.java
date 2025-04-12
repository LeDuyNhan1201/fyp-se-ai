package com.ben.smartcv.common.util;

//import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

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

}