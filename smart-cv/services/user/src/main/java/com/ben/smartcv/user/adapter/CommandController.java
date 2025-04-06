package com.ben.smartcv.user.adapter;

import com.ben.smartcv.common.contract.command.UserCommand;
import com.ben.smartcv.common.contract.dto.BaseResponse;
import com.ben.smartcv.common.util.Translator;
import com.ben.smartcv.user.application.dto.RequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.MetaData;
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

    @Operation(summary = "Sign up", description = "Sign up a new user")
    @PostMapping
    @ResponseStatus(OK)
    public ResponseEntity<BaseResponse<?, ?>> signUp(@RequestBody RequestDto.SignUp request) {
        String identifier = UUID.randomUUID().toString();
        UserCommand.CreateUser command = UserCommand.CreateUser.builder()
                .id(identifier)
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
        commandGateway.sendAndWait(command,
                MetaData.with("correlationId", identifier).and("causationId", identifier));
        return ResponseEntity.ok(BaseResponse.builder()
                .message(Translator.getMessage("SuccessMsg.Created", "New User"))
                .build());
    }

}
