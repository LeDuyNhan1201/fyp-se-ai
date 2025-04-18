package com.ben.smartcv.common.contract.dto;

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

}
