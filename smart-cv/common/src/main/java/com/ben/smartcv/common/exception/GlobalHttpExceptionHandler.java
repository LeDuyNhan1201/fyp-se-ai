package com.ben.smartcv.common.exception;

import com.ben.smartcv.common.component.Translator;
import com.ben.smartcv.common.contract.dto.BaseResponse;
import com.ben.smartcv.common.util.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@Slf4j
public class GlobalHttpExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<BaseResponse<?, ?>> handlingRuntimeException(RuntimeException exception) {
        log.error("Exception: ", exception);
        return ResponseEntity.badRequest().body(BaseResponse.builder()
                .message(Translator.getMessage("ErrorMsg.UnclassifiedError"))
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
                            Object min = getArgument((FieldError) error, 2);
                            Object max = getArgument((FieldError) error, 1);
                            if (min != null && max != null) {
                                yield Translator.getMessage(error.getDefaultMessage(), fieldAfterFormat, min, max);
                            }
                            yield Translator.getMessage(error.getDefaultMessage());
                        }
                        case "Min", "Max" -> {
                            Object value = getArgument((FieldError) error, 1);
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