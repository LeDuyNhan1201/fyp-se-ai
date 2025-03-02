package com.ben.smartcv.common.contract.command;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import static lombok.AccessLevel.PRIVATE;

public class UserCommand {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class RegisterUser {

        @TargetAggregateIdentifier
        String id;

        String userId;

        String email;

        String fullName;

    }

}
