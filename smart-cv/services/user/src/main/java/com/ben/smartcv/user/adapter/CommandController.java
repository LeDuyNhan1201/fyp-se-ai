package com.ben.smartcv.user.adapter;

import com.ben.smartcv.common.application.exception.CommonError;
import com.ben.smartcv.common.application.exception.CommonHttpException;
import com.ben.smartcv.common.contract.command.UserCommand;
import com.ben.smartcv.common.contract.dto.BaseResponse;
import com.ben.smartcv.common.contract.query.UserQuery;
import com.ben.smartcv.common.util.Translator;
import com.ben.smartcv.user.application.dto.RequestDto;
import com.ben.smartcv.user.application.dto.ResponseDto;
import com.ben.smartcv.user.application.exception.AuthError;
import com.ben.smartcv.user.application.exception.AuthHttpException;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
            UserCommand.SignUp command = UserCommand.SignUp.builder()
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

    @Operation(summary = "Sign in", description = "Get tokens for a user")
    @PostMapping("/sign-in")
    @ResponseStatus(OK)
    public ResponseEntity<ResponseDto.SignIn> signIn(@RequestBody @Valid RequestDto.SignIn request) {
        try {
            String identifier = UUID.randomUUID().toString();
            UserCommand.SignIn command = UserCommand.SignIn.builder()
                    .id(identifier)
                    .email(request.email())
                    .password(request.password())
                    .build();

            CompletableFuture<ResponseDto.SignIn> response = commandGateway.send(command,
                    MetaData.with("correlationId", identifier).and("causationId", identifier));
            return response.get().getMessage() == null
                    ? ResponseEntity.ok(response.get())
                    : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.get());

        } catch (InterruptedException | ExecutionException exception) {
            log.error("Error signing in", exception);
            throw new AuthHttpException(AuthError.SIGN_IN_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Refresh", description = "Refresh tokens for a user")
    @PostMapping("/refresh")
    @ResponseStatus(OK)
    public ResponseEntity<ResponseDto.Tokens> refresh(@RequestBody @Valid RequestDto.Refresh request) {
        try {
            String identifier = UUID.randomUUID().toString();
            UserCommand.Refresh command = UserCommand.Refresh.builder()
                    .id(identifier)
                    .refreshToken(request.refreshToken())
                    .build();

            CompletableFuture<ResponseDto.Tokens> response = commandGateway.send(command,
                    MetaData.with("correlationId", identifier).and("causationId", identifier));
            return ResponseEntity.ok(response.get());

        } catch (InterruptedException | ExecutionException exception) {
            log.error("Error signing in", exception);
            throw new AuthHttpException(AuthError.REFRESH_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
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
