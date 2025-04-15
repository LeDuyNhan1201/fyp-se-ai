package com.ben.smartcv.common.contract.event;

import com.ben.smartcv.common.contract.command.BaseCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

public class NotificationEvent {

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class NotificationSent extends BaseCommand<String> {

        String title;

        String content;

        String locale;

    }

}
