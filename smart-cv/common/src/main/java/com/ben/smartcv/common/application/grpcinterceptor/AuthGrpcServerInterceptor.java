package com.ben.smartcv.common.application.grpcinterceptor;

import com.ben.smartcv.common.infrastructure.security.SecurityGrpcProperties;
import com.ben.smartcv.common.util.Constant;
import io.grpc.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;

import java.util.List;
import java.util.stream.Stream;

import static io.grpc.Status.UNAUTHENTICATED;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE)
@Component
@GlobalServerInterceptor
public class AuthGrpcServerInterceptor implements ServerInterceptor {

    @Value("${security.jwt.access-signer-key}")
    String accessSignerKey;

    NimbusJwtDecoder nimbusJwtDecoder = null;

    final SecurityGrpcProperties securityGrpcProperties;

    public AuthGrpcServerInterceptor(SecurityGrpcProperties securityGrpcProperties) {
        this.securityGrpcProperties = securityGrpcProperties;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

        log.debug("Call to method: {}", call.getMethodDescriptor().getFullMethodName());

        List<String> staticPublicMethods = List.of(
                "ServerReflection/ServerReflectionInfo",
                "Health/Watch"
        );

        String[] methodNameRaw = call.getMethodDescriptor().getFullMethodName().split("\\.");
        String methodName = methodNameRaw[methodNameRaw.length - 1];

        List<String> publicMethods = Stream.concat(
                staticPublicMethods.stream(),
                securityGrpcProperties.getPublicMethods().stream()
        ).toList();

        for (String method : publicMethods)
            if (methodName.contains(method)) return next.startCall(call, headers);

        String token = headers.get(Constant.AUTHORIZATION_KEY);
        log.debug("Token: {}", token);

        if (token == null) {
            log.error("Token missing");
            call.close(UNAUTHENTICATED.withDescription("Token missing"), headers);
            return new ServerCall.Listener<>() {};

        } else {
            log.debug("Token: {}", token.substring(7));
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                    accessSignerKey.getBytes(),
                    Constant.JWT_SIGNATURE_ALGORITHM.getName());

            if (nimbusJwtDecoder == null) {
                nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                        .macAlgorithm(MacAlgorithm.from(Constant.JWT_SIGNATURE_ALGORITHM.getName()))
                        .build();
            }

            JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(
                    nimbusJwtDecoder.decode(token.substring(7)));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        Context ctx = Context.current().withValue(Constant.GRPC_AUTHORIZATION_CONTEXT, token);
        return Contexts.interceptCall(ctx, call, headers, next);
    }

}