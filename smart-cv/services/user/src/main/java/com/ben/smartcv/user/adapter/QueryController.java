package com.ben.smartcv.user.adapter;

import com.ben.smartcv.common.contract.query.UserQuery;
import com.ben.smartcv.user.application.dto.RequestDto;
import com.ben.smartcv.user.application.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/query")
@Tag(name = "User query APIs")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class QueryController {

    QueryGateway queryGateway;

    @Operation(summary = "Sign in", description = "Get tokens for a user")
    @PostMapping("/sign-in")
    @ResponseStatus(OK)
    public ResponseEntity<ResponseDto.Tokens> signIn(@RequestBody @Valid RequestDto.SignIn request) {
        UserQuery.SignIn query = UserQuery.SignIn.builder()
                .email(request.email())
                .password(request.password())
                .build();
        ResponseDto.Tokens tokens = queryGateway.query(query, ResponseDto.Tokens.class).join();
        return ResponseEntity.ok(tokens);
    }

    @Operation(summary = "Refresh", description = "Refresh tokens for a user")
    @PostMapping("/refresh")
    @ResponseStatus(OK)
    public ResponseEntity<ResponseDto.Tokens> refresh(@RequestBody @Valid RequestDto.Refresh request) {
        UserQuery.Refresh query = UserQuery.Refresh.builder()
                .refreshToken(request.refreshToken())
                .build();
        ResponseDto.Tokens tokens = queryGateway.query(query, ResponseDto.Tokens.class).join();
        return ResponseEntity.ok(tokens);
    }

}
