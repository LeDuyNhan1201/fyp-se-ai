package com.ben.smartcv.user.application.contract;

import lombok.*;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

public class Event {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class UserRegistered {

        String userId;

        String email;

        String fullName;

    }

}
