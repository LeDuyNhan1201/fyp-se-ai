package com.ben.smartcv.user.infrastructure.security;

import com.ben.smartcv.common.application.exception.CommonError;
import com.ben.smartcv.common.application.exception.CommonHttpException;
import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.user.application.usecase.IAuthenticationUseCase;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${security.jwt.access-signer-key}")
    private String ACCESS_SIGNER_KEY;

    private final IAuthenticationUseCase authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            if (!authenticationService.introspect(token))
                throw new CommonHttpException(CommonError.TOKEN_INVALID, UNAUTHORIZED);

        } catch (JOSEException | ParseException e) {
            log.error("Error decoding JWT:", e);
            throw new CommonHttpException(CommonError.TOKEN_INVALID, UNAUTHORIZED);
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(ACCESS_SIGNER_KEY.getBytes(),
                    Constant.JWT_SIGNATURE_ALGORITHM.getName());
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.from(Constant.JWT_SIGNATURE_ALGORITHM.getName()))
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }

}