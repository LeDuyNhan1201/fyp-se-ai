package com.ben.smartcv.common.application.exception;

import com.ben.smartcv.common.util.LogHelper;
import com.ben.smartcv.common.util.Translator;
import com.ben.smartcv.common.contract.dto.BaseResponse;
import com.ben.smartcv.common.util.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Order()
@Slf4j
public class GlobalHttpExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<BaseResponse<?, ?>> handlingRuntimeException(RuntimeException exception) {
        log.error("Exception: ", exception);
        return ResponseEntity.badRequest().body(BaseResponse.builder()
                .message(Translator.getMessage("ErrorMsg.UnclassifiedError"))
                .build());
    }

    @ExceptionHandler(value = CommonHttpException.class)
    public ResponseEntity<?> handle(CommonHttpException exception) {
        LogHelper.logError(log, exception.getMessage(), exception);
        CommonError error = exception.getError();
        return ResponseEntity.status(exception.getHttpStatus()).body(BaseResponse.builder()
                .errorCode(error.getCode())
                .message((exception.getMoreInfo() != null)
                        ? Translator.getMessage(error.getMessage(), exception.getMoreInfo())
                        : Translator.getMessage(error.getMessage()))
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<?, ?>>
    handleMethodArgumentNotValidExceptions(MethodArgumentNotValidException e) {
        log.error("Validation Exception: ", e);
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach((error) -> {
                    String field = ((FieldError) error).getField();
                    String fieldAfterFormat = StringHelper.formatFieldName(field);
                    String validationType = determineValidationType((FieldError) error);

                    assert validationType != null;
                    String message = switch (validationType) {
                        case "NotBlank", "NotNull" -> Translator.getMessage(error.getDefaultMessage(), fieldAfterFormat);
                        case "Size" -> {
                            String min = Objects.requireNonNull(getArgument((FieldError) error, 2)).toString();
                            String max = Objects.requireNonNull(getArgument((FieldError) error, 1)).toString();
                            if (min != null && max != null) {
                                yield Translator.getMessage(error.getDefaultMessage(), fieldAfterFormat, min, max);
                            }
                            yield Translator.getMessage(error.getDefaultMessage());
                        }
                        case "Min", "Max" -> {
                            String value = Objects.requireNonNull(getArgument((FieldError) error, 1)).toString();
                            if (value != null) {
                                yield Translator.getMessage(error.getDefaultMessage(), fieldAfterFormat, value);
                            }
                            yield Translator.getMessage(error.getDefaultMessage());
                        }
                        case "Unknown" -> "Unknown validation error";
                        default -> Translator.getMessage(error.getDefaultMessage());
                    };
                    errors.put(field, message);
                });

        return ResponseEntity.status(BAD_REQUEST).body(
                BaseResponse.builder()
                        .errorCode(CommonError.VALIDATION_ERROR.getCode())
                        .message(Translator.getMessage(CommonError.VALIDATION_ERROR.getMessage()))
                        .errors(errors)
                        .build()
        );
    }

    private String determineValidationType(FieldError fieldError) {
        // Lấy mã lỗi chính (ví dụ: "NotNull", "Size", "Min", "Max", ...)
        // Có thể là getCode() hoặc getCodes()[0] tùy vào cấu hình
        String[] codes = fieldError.getCodes();
        if (codes != null && codes.length > 0) {
            // Mã lỗi thường ở cuối mảng codes
            return codes[codes.length - 1];
        }
        return "Unknown";
    }

    private Object getArgument(FieldError fieldError, int index) {
        Object[] args = fieldError.getArguments();
        if (args != null && args.length > index) {
            return args[index];
        }
        return null;
    }

}