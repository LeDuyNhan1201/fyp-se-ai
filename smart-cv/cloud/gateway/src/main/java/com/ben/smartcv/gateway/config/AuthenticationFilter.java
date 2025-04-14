package com.ben.smartcv.gateway.config;

import com.ben.smartcv.gateway.dto.CommonResponse;
import com.ben.smartcv.gateway.exception.AppError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {

    ObjectMapper objectMapper;

    String[] publicEndpoints = {
            "/user/command/sign-in",
            "/user/command/refresh",
            "/user/command/sign-up",
            "/user/command/sign-out",

            "/job/graphiql",
            "/job/graphql",
            "/job/**",
            "/cv/graphiql",
            "/cv/graphql",
            "/cv/**",

            "/cv/api-docs/**",
            "/file/api-docs/**",
            "/job/api-docs/**",
            "/user/api-docs/**",
            "/notification/api-docs/**",
    };

    List<Locale> LOCALES = List.of(
            Locale.forLanguageTag("en")
            //Locale.forLanguageTag("vi")
    );

    @Bean
    public MessageSource messageSource(
            @Value("${spring.messages.basename}") String basename,
            @Value("${spring.messages.encoding}") String encoding,
            @Value("${spring.messages.default-locale}") String defaultLocale,
            @Value("${spring.messages.cache-duration}") int cacheSeconds
    ) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(basename);
        messageSource.setDefaultEncoding(encoding);
        messageSource.setDefaultLocale(Locale.of(defaultLocale));
        messageSource.setCacheSeconds(cacheSeconds);
        return messageSource;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter authentication filter....");

        String headerLang = exchange.getRequest().getHeaders().getFirst("Accept-Language");
        log.info("Accept-Language: {}", headerLang);
        Locale locale = StringUtils.hasLength(headerLang)
                ? Locale.lookup(Locale.LanguageRange.parse(headerLang), LOCALES)
                : Locale.getDefault();

        LocaleContextHolder.setLocale(locale);
        log.info("Resolved Locale: {}", LocaleContextHolder.getLocale());

        log.info("Request Path: {}", exchange.getRequest().getPath());
        if (isPublicEndpoint(exchange.getRequest())) return chain.filter(exchange);

        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader)) return unauthenticated(exchange.getResponse());

        String token = authHeader.getFirst().replace("Bearer ", "");
        log.info("Bearer Token: {}", token);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        log.info("Checking if endpoint is public...");
        PathMatcher pathMatcher = new AntPathMatcher();
        String requestPath = request.getURI().getPath();
        log.info("BOOL: {}", Arrays.stream(publicEndpoints).anyMatch(pattern -> pathMatcher.match(pattern, requestPath)));
        return Arrays.stream(publicEndpoints).anyMatch(pattern -> pathMatcher.match(pattern, requestPath));
    }

    private Mono<Void> unauthenticated(ServerHttpResponse response) {
        AppError error = AppError.TOKEN_MISSING;
        CommonResponse<?, ?> apiResponse = CommonResponse.builder()
                .errorCode(error.getCode())
                .message(Translator.getMessage(error.getMessage()))
                .build();

        String body;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

}