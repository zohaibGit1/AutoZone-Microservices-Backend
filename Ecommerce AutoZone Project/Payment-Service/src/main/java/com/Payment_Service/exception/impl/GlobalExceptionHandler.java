package com.Payment_Service.exception.impl;

import com.Payment_Service.dto.ErrorResponse;
import com.Payment_Service.exception.PaymentNotFoundException;
import com.Payment_Service.exception.PaymentProcessingException;
import com.razorpay.RazorpayException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFoundException(PaymentNotFoundException ex) {
        log.error("Payment not found: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<ErrorResponse> handlePaymentProcessingException(PaymentProcessingException ex) {
        log.error("Payment processing error: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(RazorpayException.class)
    public ResponseEntity<ErrorResponse> handleRazorpayException(RazorpayException ex) {
        log.error("Razorpay gateway error: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.BAD_GATEWAY, "Payment gateway error: " + ex.getMessage(), null);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex) {
        log.error("Feign client communication error: {}", ex.getMessage());
        HttpStatus status = HttpStatus.resolve(ex.status());
        if (status == null || status.is5xxServerError()) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
        }
        return buildResponse(status, "External service communication error: " + ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );
        log.warn("Validation failed: {}", fieldErrors);
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", fieldErrors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON request body: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request body", null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Invalid value '" + ex.getValue() + "' for parameter '" + ex.getName() + "'";
        log.warn(message);
        return buildResponse(HttpStatus.BAD_REQUEST, message, null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex) {
        log.warn("Illegal state: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Data integrity violation: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, "Database constraint violation: duplicate record or invalid reference", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong. Please try again later.", null);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, Map<String, String> fieldErrors) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .fieldErrors(fieldErrors)
                .build();
        return new ResponseEntity<>(error, status);
    }
}
