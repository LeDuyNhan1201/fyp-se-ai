package com.ben.smartcv.notification.application.contract;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import static lombok.AccessLevel.PRIVATE;

public class Command {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class RegisterUser {

        @TargetAggregateIdentifier
        String userId;

        String email;

        String fullName;

    }

}
