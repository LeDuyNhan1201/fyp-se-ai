package com.ben.smartcv.common.contract.dto;

import lombok.Getter;

public class Enum {

    public enum MailType {

        APPROVAL("Approval"),

        REJECTION("Rejection"),

        INTERVIEW("Interview"),

        OFFER("Offer"),

        ;

        MailType(String value) {
            this.value = value;
        }

        final String value;

    }

    @Getter
    public enum CvStatus {
        PENDING("Pending"),
        APPROVED("Approved"),
        REJECTED("Rejected"),

        ;
        CvStatus(String value) {
            this.value = value;
        }

        final String value;
    }

}
