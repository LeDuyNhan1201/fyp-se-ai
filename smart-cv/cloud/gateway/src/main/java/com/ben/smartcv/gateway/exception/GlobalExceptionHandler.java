package com.ben.smartcv.gateway.exception;

import com.ben.smartcv.gateway.dto.CommonResponse;
import com.ben.smartcv.gateway.controller.FallbackController;
import com.ben.smartcv.gateway.config.Translator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice(assignableTypes = { FallbackController.class })
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalExceptionHandler {

    ObjectMapper objectMapper;

    @ExceptionHandler(value = RuntimeException.class)
    Mono<Void> handlingRuntimeException(RuntimeException exception, ServerHttpResponse response) {
        log.error("Exception: ", exception);
        CommonResponse<?, ?> apiResponse = CommonResponse.builder()
                .message(Translator.getMessage("ErrorMsg.UnclassifiedError"))
                .build();

        String body;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    @ExceptionHandler(value = NoSuchMessageException.class)
    Mono<Void> handlingNoSuchMessageException(NoSuchMessageException exception, ServerHttpResponse response) {
        errorLogging(exception.getMessage(), exception);
        CommonResponse<?, ?> apiResponse = CommonResponse.builder()
                .message(exception.getMessage())
                .build();

        String body;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    @ExceptionHandler(value = AppException.class)
    Mono<Void> handlingAppException(AppException exception, ServerHttpResponse response) {
        errorLogging(exception.getMessage(), exception);
        AppError errorCode = exception.getError();

        CommonResponse<?, ?> apiResponse = CommonResponse.builder()
                .errorCode(errorCode.getCode())
                .message((exception.getMoreInfo() != null)
                        ? Translator.getMessage(errorCode.getMessage(), exception.getMoreInfo())
                        : Translator.getMessage(errorCode.getMessage()))
                .build();

        String body;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(exception.getHttpStatus());
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    private void errorLogging(String reason, Exception exception) {
        log.error("Reason: {} | class: {} | line: {}",
                reason, exception.getClass(), exception.getStackTrace()[0].getLineNumber());
    }

}