package com.ben.smartcv.user.adapter;

import com.ben.smartcv.common.application.exception.CommonError;
import com.ben.smartcv.common.application.exception.CommonHttpException;
import com.ben.smartcv.common.contract.command.UserCommand;
import com.ben.smartcv.common.contract.dto.BaseResponse;
import com.ben.smartcv.common.util.Translator;
import com.ben.smartcv.user.application.dto.RequestDto;
import com.ben.smartcv.user.application.usecase.IAuthenticationUseCase;
import com.ben.smartcv.user.application.usecase.IUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.MetaData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/command")
@Tag(name = "User command APIs")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CommandController {

    CommandGateway commandGateway;

    IAuthenticationUseCase authenticationUseCase;

    IUserUseCase userUseCase;

    @Operation(summary = "Sign up", description = "Sign up a new user")
    @PostMapping("/sign-up")
    @ResponseStatus(OK)
    public ResponseEntity<BaseResponse<?, ?>> signUp(@RequestBody @Valid RequestDto.SignUp request) {
        authenticationUseCase.validateSignUp(request);
        try {
            String identifier = UUID.randomUUID().toString();
            UserCommand.SignUpUser command = UserCommand.SignUpUser.builder()
                    .id(identifier)
                    .email(request.email())
                    .password(request.password())
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .build();

            commandGateway.sendAndWait(command,
                    MetaData.with("correlationId", identifier).and("causationId", identifier));

            return ResponseEntity.ok(BaseResponse.builder()
                    .message(Translator.getMessage("SuccessMsg.Created", "New User"))
                    .build());

        } catch (Exception exception) {
            log.error("Error creating user", exception);
            throw new CommonHttpException(CommonError.CREATE_FAILED, HttpStatus.CONFLICT, "User");
        }
    }

    @Operation(summary = "Seed data", description = "Seed data for testing")
    @GetMapping("/seed/{count}")
    @ResponseStatus(OK)
    public ResponseEntity<BaseResponse<?, ?>> seedData(@PathVariable int count) {
        try {
            userUseCase.seed(count);
            return ResponseEntity.ok(BaseResponse.builder()
                    .message(Translator.getMessage("SuccessMsg.Created", count + " user(s)"))
                    .build());

        } catch (Exception exception) {
            log.error("Error seeding users", exception);
            throw new CommonHttpException(CommonError.CREATE_FAILED, HttpStatus.CONFLICT, "User(s)");
        }
    }

}
