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
import javax.crypto.spec.SecretKeySpec;
import java.util.List;

import static io.grpc.Status.UNAUTHENTICATED;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE)
@GlobalServerInterceptor
public class AuthGrpcServerInterceptor implements ServerInterceptor {

    @Value("${security.jwt.access-signer-key}")
    String accessSignerKey;

    @Value("${spring.application.name}")
    String microserviceName;

    NimbusJwtDecoder nimbusJwtDecoder = null;

    SecurityGrpcProperties securityGrpcProperties;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

        String[] methodNameRaw = call.getMethodDescriptor().getFullMethodName().split("\\.");
        String methodName = methodNameRaw[methodNameRaw.length - 1];

        for (String method : securityGrpcProperties.getPublicMethods())
            if (methodName.contains(method)) return next.startCall(call, headers);

        String token = headers.get(Constant.AUTHORIZATION_KEY);
        log.info("[{}]: token: {}", microserviceName, token);

        if (token == null) {
            log.error("[{}]: Token missing", microserviceName);
            call.close(UNAUTHENTICATED.withDescription("Token missing"), headers);
            return new ServerCall.Listener<>() {};

        } else {
            log.info("[{}] Token: {}", microserviceName, token.substring(7));
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                    accessSignerKey.getBytes(),
                    Constant.JWT_SIGNATURE_ALGORITHM.getName());

            if (nimbusJwtDecoder == null) {
                nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                        .macAlgorithm(MacAlgorithm.from(Constant.JWT_SIGNATURE_ALGORITHM.getName()))
                        .build();
            }

            JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(nimbusJwtDecoder.decode(token.substring(7)));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        Context ctx = Context.current().withValue(Constant.GRPC_AUTHORIZATION_CONTEXT, token);
        return Contexts.interceptCall(ctx, call, headers, next);
    }

}