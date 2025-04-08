package com.ben.smartcv.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RequestDto {

    public record SignUp (

        @NotNull(message = "Validation.Null")
        @NotBlank(message = "Validation.Blank")
        @Email(message = "Validation.Email")
        String email,

        @NotNull(message = "Validation.Null")
        @NotBlank(message = "Validation.Blank")
        @Size(min = 6, max = 20, message = "Validation.Size")
        String password,

        @NotNull(message = "Validation.Null")
        @NotBlank(message = "Validation.Blank")
        @Size(min = 6, max = 20, message = "Validation.Size")
        String confirmationPassword,

        @NotNull(message = "Validation.Null")
        @NotBlank(message = "Validation.Blank")
        String firstName,

        @NotNull(message = "Validation.Null")
        @NotBlank(message = "Validation.Blank")
        String lastName,

        boolean acceptTerms

    ) { }

    public record SignIn (

        @NotNull(message = "Validation.Null")
        @NotBlank(message = "Validation.Blank")
        @Email(message = "Validation.Email")
        String email,

        @NotNull(message = "Validation.Null")
        @NotBlank(message = "Validation.Blank")
        @Size(min = 6, max = 20, message = "Validation.Size")
        String password

    ) { }

    public record Refresh (

            @NotNull(message = "Validation.Null")
            @NotBlank(message = "Validation.Blank")
            String refreshToken

    ) { }

}
