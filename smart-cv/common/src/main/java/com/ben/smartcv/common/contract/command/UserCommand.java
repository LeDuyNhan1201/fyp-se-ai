package com.ben.smartcv.common.contract.command;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

public class UserCommand {

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class SignUpUser extends BaseCommand<String> {

        String email;

        String password;

        String confirmPassword;

        String firstName;

        String lastName;

        boolean acceptTerms;

    }

}
