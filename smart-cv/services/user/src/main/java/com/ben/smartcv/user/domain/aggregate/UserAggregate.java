package com.ben.smartcv.user.domain.aggregate;

import com.ben.smartcv.common.contract.command.UserCommand;
import com.ben.smartcv.common.contract.event.UserEvent;
import com.ben.smartcv.common.util.LogHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.MetaData;
import org.axonframework.messaging.annotation.MetaDataValue;
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
    String id;

    String userId;

    String email;

    String password;

    String confirmPassword;

    String firstName;

    String lastName;

    boolean acceptTerms;

    @CommandHandler
    public UserAggregate(UserCommand.SignUp command,
                         @MetaDataValue("correlationId") String correlationId,
                         @MetaDataValue("causationId") String causationId) {
        LogHelper.logMessage(log, "SignUp", correlationId, causationId, command);
        apply(UserEvent.SignedUp.builder()
                .id(command.getId())
                .email(command.getEmail())
                .password(command.getPassword())
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .build(), MetaData.with("correlationId", command.getId()).and("causationId", correlationId));
    }

    @EventSourcingHandler
    public void on(UserEvent.SignedUp event) {
        this.id = event.getId();
        this.email = event.getEmail();
        this.password = event.getPassword();
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
    }

    @ExceptionHandler(resultType = IllegalStateException.class, payloadType = UserCommand.SignUp.class)
    public void handleExceptionForSignUpCommand(Exception exception) {
        log.error("Unexpected Error when signing up: {}", exception.getMessage());
    }

}
