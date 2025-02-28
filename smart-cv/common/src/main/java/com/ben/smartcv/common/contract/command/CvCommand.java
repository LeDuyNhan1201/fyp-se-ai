package com.ben.smartcv.common.contract.command;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import static lombok.AccessLevel.PRIVATE;

public class CvCommand {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class ApplyCv {

        @TargetAggregateIdentifier
        String cvId;

        String userId;

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class ParseCv {

        @TargetAggregateIdentifier
        String cvId;

    }

}
