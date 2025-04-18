package com.ben.smartcv.curriculum_vitae.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RequestDto {

    public record ApproveCv(

            String title,

            String content,

            @NotNull(message = "Validation.Null")
            @NotBlank(message = "Validation.Blank")
            String receiverId,

            @NotNull(message = "Validation.Null")
            @NotBlank(message = "Validation.Blank")
            String cvId

    ) { }

}
