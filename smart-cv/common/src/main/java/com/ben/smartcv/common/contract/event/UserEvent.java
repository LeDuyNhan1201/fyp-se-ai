package com.ben.smartcv.common.contract.event;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

public class UserEvent {

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class UserRegistered extends BaseEvent<String> {

        String email;

        String password;

        String firstName;

        String lastName;

    }

}
