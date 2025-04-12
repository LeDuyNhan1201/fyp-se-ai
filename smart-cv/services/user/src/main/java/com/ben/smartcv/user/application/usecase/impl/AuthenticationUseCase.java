package com.ben.smartcv.user.application.usecase.impl;

import com.ben.smartcv.common.application.exception.CommonError;
import com.ben.smartcv.common.application.exception.CommonHttpException;
import com.ben.smartcv.common.auth.AuthServiceGrpc;
import com.ben.smartcv.common.auth.IntrospectRequest;
import com.ben.smartcv.common.auth.IntrospectResponse;
import com.ben.smartcv.common.contract.event.UserEvent;
import com.ben.smartcv.common.contract.query.UserQuery;
import com.ben.smartcv.common.util.Translator;
import com.ben.smartcv.user.application.dto.RequestDto;
import com.ben.smartcv.user.application.dto.ResponseDto;
import com.ben.smartcv.user.application.exception.AuthError;
import com.ben.smartcv.user.application.exception.AuthHttpException;
import com.ben.smartcv.user.application.usecase.IAuthenticationUseCase;
import com.ben.smartcv.user.application.usecase.IBaseRedisUseCase;
import com.ben.smartcv.user.application.usecase.IUserUseCase;
import com.ben.smartcv.user.domain.entity.User;
import com.ben.smartcv.user.util.Constant;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.grpc.stub.StreamObserver;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.time.Instant;
import java.util.*;

import static com.ben.smartcv.user.application.exception.AuthError.WRONG_PASSWORD;
import static java.time.temporal.ChronoUnit.SECONDS;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationUseCase extends AuthServiceGrpc.AuthServiceImplBase implements IAuthenticationUseCase  {

    IUserUseCase userUseCase;

    PasswordEncoder passwordEncoder;

    IBaseRedisUseCase<String, String, Object> redisUseCase;

    CommandGateway commandGateway;

    @NonFinal
    @Value("${security.jwt.access-signer-key}")
    String ACCESS_SIGNER_KEY;

    @NonFinal
    @Value("${security.jwt.refresh-signer-key}")
    String REFRESH_SIGNER_KEY;

    @NonFinal
    @Value("${security.jwt.valid-duration}")
    long VALID_DURATION;

    @NonFinal
    @Value("${security.jwt.refreshable-duration}")
    long REFRESHABLE_DURATION;

    @Override
    public void introspect(IntrospectRequest request, StreamObserver<IntrospectResponse> responseObserver) {
        // Get the token from the request
        String token = request.getToken();

        // Initialize response builder
        IntrospectResponse.Builder responseBuilder = IntrospectResponse.newBuilder();

        try {
            boolean isValid = introspect(token);
            responseBuilder.setValid(isValid);

        } catch (JOSEException | ParseException e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription(String.format("Token parsing or validation error: %s", e.getMessage()))
                    .asRuntimeException());
            return;
        }

        // Send the response
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public boolean introspect(String token) throws JOSEException, ParseException {
        boolean isValid = true;

        try {
            verifyToken(token, false);

        } catch (AuthHttpException e) {
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void validateSignUp(RequestDto.SignUp request) {
        if (userUseCase.existsByEmail(request.email()))
            throw new AuthHttpException(AuthError.EMAIL_ALREADY_IN_USE, HttpStatus.CONFLICT);

        if (!request.password().equals(request.confirmationPassword()))
            throw new AuthHttpException(AuthError.PASSWORD_MIS_MATCH, HttpStatus.BAD_REQUEST);

        if (!request.acceptTerms())
            throw new AuthHttpException(AuthError.TERMS_NOT_ACCEPTED, HttpStatus.BAD_REQUEST);
    }

    @Override
    public void signUp(UserEvent.UserSignedUp event) {
        User user = User.builder()
                .email(event.getEmail())
                .password(passwordEncoder.encode(event.getPassword()))
                .firstName(event.getFirstName())
                .lastName(event.getLastName())
                .build();
        userUseCase.create(user);
    }

    @Override
    public ResponseDto.Tokens signIn(UserQuery.SignIn query) {
        User user = userUseCase.findByEmail(query.getEmail());

//        if (!passwordEncoder.matches(query.getPassword(), user.getPassword()))
//            throw new AuthHttpException(WRONG_PASSWORD, HttpStatus.UNAUTHORIZED);

        boolean isValid = passwordEncoder.matches(query.getPassword(), user.getPassword());
        ResponseDto.Tokens tokens = ResponseDto.Tokens.builder()
                .accessToken(isValid ? generateToken(user, false) : null)
                .refreshToken(isValid ? generateToken(user, true) : null)
                .message(!isValid ? Translator.getMessage(WRONG_PASSWORD.getMessage()) : null)
                .build();
        return tokens;
    }

    @Override
    public ResponseDto.Tokens refresh(UserQuery.Refresh query) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(query.getRefreshToken(), true);
        String id = signedJWT.getJWTClaimsSet().getSubject();
        String jti = signedJWT.getJWTClaimsSet().getJWTID();
        String message = null;
        User user = null;
        try {
            user = userUseCase.findById(id);

        } catch (AuthHttpException e) {
            //throw new CommonHttpException(CommonError.RESOURCE_NOT_FOUND, HttpStatus.UNAUTHORIZED, "User");
            message = Translator.getMessage(CommonError.RESOURCE_NOT_FOUND.getMessage(), "User");
        }

        if (user == null)
            message = Translator.getMessage(CommonError.RESOURCE_NOT_FOUND.getMessage(), "User");

        ResponseDto.Tokens tokens = ResponseDto.Tokens.builder()
                .refreshToken(message == null ?  generateToken(user, true) : null)
                .message(message)
                .build();

        redisUseCase.set(jti, "revoked");
        redisUseCase.setTimeToLive(jti,
                signedJWT.getJWTClaimsSet().getExpirationTime().getTime() - System.currentTimeMillis());

        return tokens;
    }

    @Override
    public void signOut(String accessToken, String refreshToken) throws ParseException, JOSEException {
        try {
            SignedJWT signAccessToken = verifyToken(accessToken, false);
            Date AccessTokenExpiryTime = signAccessToken.getJWTClaimsSet().getExpirationTime();

            if (AccessTokenExpiryTime.after(new Date())) {
                redisUseCase.set(signAccessToken.getJWTClaimsSet().getJWTID(), "revoked");
                redisUseCase.setTimeToLive(signAccessToken.getJWTClaimsSet().getJWTID(),
                        AccessTokenExpiryTime.getTime() - System.currentTimeMillis());
            }

            SignedJWT signRefreshToken = verifyToken(refreshToken, true);
            Date RefreshTokenExpiryTime = signRefreshToken.getJWTClaimsSet().getExpirationTime();

            if (RefreshTokenExpiryTime.after(new Date())) {
                redisUseCase.set(signRefreshToken.getJWTClaimsSet().getJWTID(), "revoked");
                redisUseCase.setTimeToLive(signRefreshToken.getJWTClaimsSet().getJWTID(),
                        RefreshTokenExpiryTime.getTime() - System.currentTimeMillis());
            }

        } catch (AuthHttpException exception) {
            log.error("Cannot sign out", exception);
            //TODO: Disable the user account
        }
    }

    private String generateToken(User user, boolean isRefresh) {
        JWSHeader accessHeader = new JWSHeader(com.ben.smartcv.common.util.Constant.ACCESS_TOKEN_SIGNATURE_ALGORITHM);
        JWSHeader refreshHeader = new JWSHeader(Constant.REFRESH_TOKEN_SIGNATURE_ALGORITHM);

        Date expiryTime = (isRefresh)
                ? new Date(Instant.now().plus(REFRESHABLE_DURATION, SECONDS).toEpochMilli())
                : new Date(Instant.now().plus(VALID_DURATION, SECONDS).toEpochMilli());

        String jwtID = UUID.randomUUID().toString();

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("com.ben.smart-cv")
                .issueTime(new Date())
                .expirationTime(expiryTime)
                .jwtID(jwtID)
                .build();

        if (!isRefresh) {
            jwtClaimsSet = new JWTClaimsSet.Builder(jwtClaimsSet).build();
        }

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = (isRefresh)
                ? new JWSObject(refreshHeader, payload)
                : new JWSObject(accessHeader, payload);

        try {
            if (isRefresh)
                jwsObject.sign(new MACSigner(REFRESH_SIGNER_KEY.getBytes()));
            else
                jwsObject.sign(new MACSigner(ACCESS_SIGNER_KEY.getBytes()));

            return jwsObject.serialize();

        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new CommonHttpException(CommonError.SIGNATURE_INVALID, HttpStatus.UNAUTHORIZED);
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = (isRefresh)
                ? new MACVerifier(REFRESH_SIGNER_KEY.getBytes())
                : new MACVerifier(ACCESS_SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        if (isRefresh) {
            if (expiryTime.before(new Date()))
                throw new CommonHttpException(CommonError.TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED);

            if (!verified)
                throw new CommonHttpException(CommonError.SIGNATURE_INVALID, HttpStatus.UNAUTHORIZED);

            SecretKeySpec secretKeySpec = new SecretKeySpec(
                    REFRESH_SIGNER_KEY.getBytes(),
                    Constant.REFRESH_TOKEN_SIGNATURE_ALGORITHM.getName()
            );
            try {
                NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                        .macAlgorithm(MacAlgorithm.from(Constant.REFRESH_TOKEN_SIGNATURE_ALGORITHM.getName()))
                        .build();
                nimbusJwtDecoder.decode(token);

            } catch (JwtException e) {
                throw new CommonHttpException(CommonError.SIGNATURE_INVALID, HttpStatus.UNAUTHORIZED);
            }

        } else {
            if (!verified || expiryTime.before(new Date()))
                throw new CommonHttpException(CommonError.TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
        }

        String value = (String) redisUseCase.get(signedJWT.getJWTClaimsSet().getJWTID());

        if (value != null) {
            if (value.equals("revoked"))
                throw new CommonHttpException(CommonError.TOKEN_REVOKED, HttpStatus.UNAUTHORIZED);
        }

        return signedJWT;
    }

}