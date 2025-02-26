package com.ben.smartcv.user.domain;

import com.ben.smartcv.common.contract.command.UserCommand;
import com.ben.smartcv.common.contract.event.UserEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.MetaData;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static lombok.AccessLevel.PRIVATE;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Aggregate
@FieldDefaults(level = PRIVATE)
public class UserAggregate {

    @AggregateIdentifier
    String userId;

    String email;

    String fullName;

    @CommandHandler
    public UserAggregate(UserCommand.RegisterUser command) {
        if (command.getEmail().isEmpty()) {
            throw new IllegalStateException("Email cannot be empty");
        }
        apply(UserEvent.UserRegistered.builder()
                .userId(command.getUserId())
                .email(command.getEmail())
                .fullName(command.getFullName())
                .build(), MetaData.with("key", "123"));
    }

    @EventSourcingHandler
    public void on(UserEvent.UserRegistered event) {
        this.userId = event.getUserId();
        this.email = event.getEmail();
        this.fullName = event.getFullName();
    }

    @ExceptionHandler(resultType = IllegalStateException.class, payloadType = UserCommand.RegisterUser.class)
    public void handleIllegalStateExceptionsFromIssueCard(Exception exception) {
        log.error("IllegalStateException occurred: {}", exception.getMessage());
    }

}
