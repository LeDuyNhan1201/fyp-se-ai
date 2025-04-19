package com.ben.smartcv.job.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class RequestDto {

    public record CreateJobDescription(

        @NotNull(message = "Validation.Null")
        @NotBlank(message = "Validation.Blank")
        String organizationName,

        @NotNull(message = "Validation.Null")
        @NotBlank(message = "Validation.Blank")
        String position,

        @NotNull(message = "Validation.Null")
        Date expiredAt,

        @NotNull(message = "Validation.Null")
        @Min(value = 0, message = "Validation.Min")
        Double fromSalary,

        @NotNull(message = "Validation.Null")
        @Min(value = 1, message = "Validation.Min")
        Double toSalary,

        @NotNull(message = "Validation.Null")
        @NotBlank(message = "Validation.Blank")
        String requirements

    ) { }

}
