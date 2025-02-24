package com.ben.smartcv.user.adapter;

import com.ben.smartcv.user.application.contract.Command;
import com.ben.smartcv.user.application.dto.RequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.MetaData;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/test")
@Tag(name = "Test APIs")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserController {

    CommandGateway commandGateway;

    @Operation(summary = "Hello", description = "First API")
    @PostMapping
    @ResponseStatus(OK)
    public CompletableFuture<String> hello(@RequestBody RequestDto.CreateUser request) {
        Command.RegisterUser command = Command.RegisterUser.builder()
                .userId(UUID.randomUUID().toString())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .build();
        return commandGateway.send(command, MetaData.with("key", "123"));
    }

}
