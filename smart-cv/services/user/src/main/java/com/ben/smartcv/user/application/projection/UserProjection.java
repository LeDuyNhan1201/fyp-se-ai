package com.ben.smartcv.user.application.projection;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserProjection {

//    @QueryHandler
//    public ResponseDto.Tokens handle(UserQuery.Refresh query) {
//        try {
//            //return authenticationUseCase.refresh(query);
//            return null;
//
//        } catch (Exception e) {
//            log.error("Error while refreshing token: {}", e.getMessage());
//            throw new CommonHttpException(CommonError.TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
//        }
//    }

}
