package com.ben.smartcv.notification.application.dto;

public class RequestDto {

    public record SendApprovalMail(

            String title,

            String content

    ) { }

}
