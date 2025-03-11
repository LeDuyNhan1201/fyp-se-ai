package com.ben.smartcv.job.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RequestDto {

    public record CreateJobDescription(

        @NotNull(message = "Validation.Null")
        @NotBlank(message = "Validation.Blank")
        String organizationName,

        @NotNull(message = "Validation.Null")
        @NotBlank(message = "Validation.Blank")
        String position,

        @NotNull(message = "Validation.Null")
        @NotBlank(message = "Validation.Blank")
        String requirements

    ) { }

}
