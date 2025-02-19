package com.ben.novax.user.domain;

import com.ben.novax.user.application.contract.Command;
import com.ben.novax.user.application.contract.Event;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
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
    public UserAggregate(Command.RegisterUser command) {
        apply(Event.UserRegistered.builder()
                .userId(command.getUserId())
                .email(command.getEmail())
                .fullName(command.getFullName())
                .build());
    }

    @EventSourcingHandler
    public void on(Event.UserRegistered event) {
        this.userId = event.getUserId();
        this.email = event.getEmail();
        this.fullName = event.getFullName();
    }

}
