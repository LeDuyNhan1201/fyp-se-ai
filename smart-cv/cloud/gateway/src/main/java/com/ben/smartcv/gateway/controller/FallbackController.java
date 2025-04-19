package com.ben.smartcv.gateway.controller;

import com.ben.smartcv.gateway.exception.AppError;
import com.ben.smartcv.gateway.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping("/cv/fallback")
    public Mono<Void> cvServiceFallback() {
        throw new AppException(AppError.FALLBACK_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, "CV");
    }

    @GetMapping("/file/fallback")
    public Mono<Void> fileServiceFallback() {
        throw new AppException(AppError.FALLBACK_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, "File");
    }

    @GetMapping("/job/fallback")
    public Mono<Void> jobServiceFallback() {
        throw new AppException(AppError.FALLBACK_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, "Job");
    }

    @GetMapping("/user/fallback")
    public Mono<Void> userServiceFallback() {
        throw new AppException(AppError.FALLBACK_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, "User");
    }

    @GetMapping("/notification/fallback")
    public Mono<Void> notificationServiceFallback() {
        throw new AppException(AppError.FALLBACK_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, "Notification");
    }

}