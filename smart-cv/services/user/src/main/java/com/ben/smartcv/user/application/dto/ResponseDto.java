package com.ben.smartcv.user.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

import static lombok.AccessLevel.PRIVATE;

public class ResponseDto {

    @Getter
    @Builder
    @FieldDefaults(level = PRIVATE)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Tokens implements Serializable {

        String accessToken;

        String refreshToken;

        String message;

    }

    @Getter
    @Builder
    @FieldDefaults(level = PRIVATE)
    public static class PreviewUser implements Serializable {

        String email;

        String name;

    }

    @Getter
    @Builder
    @FieldDefaults(level = PRIVATE)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SignIn implements Serializable {

        Tokens tokens;

        PreviewUser user;

        String message;

    }

}
