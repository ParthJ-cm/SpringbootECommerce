package com.shop.user_service.exceptionhandler;

import com.shop.user_service.exceptions.BaseException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiError> handleBaseException (BaseException exception){
        ApiError apiError = ApiError.builder()
                .statusCode(exception.getStatus().value())
                .error(exception.getStatus().getReasonPhrase())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(apiError, exception.getStatus());
    }

    /**
     * Handles validation errors for @Valid @RequestBody
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        Function<FieldError, String> safeMessage = fe ->
                fe.getDefaultMessage() == null ? "Validation error" : fe.getDefaultMessage();

        Map<String, String> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        safeMessage,
                        (msg1, msg2) -> msg1 + "; " + msg2));

        ApiError apiError = ApiError.builder()
                .statusCode(status.value())
                .error(status.getReasonPhrase())
                .message("Validation failed for one or more fields.")
                .details(fieldErrors)
                .build();
        return new ResponseEntity<>(apiError, status);
    }

    /**
     * Handles validation errors for @Valid @PathVariable, @RequestParam, or @Validated method params
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(v -> v.getPropertyPath().toString(), v -> v.getMessage()));

        ApiError apiError = ApiError.builder()
                .statusCode(status.value())
                .error(status.getReasonPhrase())
                .message("Validation failed for one or more fields.")
                .details(errors)
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiError apiError = ApiError.builder()
                .statusCode(status.value())
                .error(status.getReasonPhrase())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception exception){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiError apiError = ApiError.builder()
                .statusCode(status.value())
                .error(status.getReasonPhrase())
                .message("An unexpected error occurred : " + exception.getMessage())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}