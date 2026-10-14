package com.example.statemachine.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ProblemDetail getProblemDetails(HttpStatus status, String title, String detail, HttpServletRequest request) {
        ProblemDetail problemDetails = ProblemDetail.forStatus(status);
        problemDetails.setTitle(title);
        problemDetails.setDetail(detail);
        problemDetails.setInstance(URI.create(request.getRequestURI()));
        return problemDetails;
    }

    @ExceptionHandler(ApiException.class)
    public ProblemDetail handleApi(ApiException ex, HttpServletRequest request) {
        ProblemDetail problemDetails = getProblemDetails(ex.status(), ex.title(), ex.getMessage(), request);
        ex.properties().forEach(
                problemDetails::setProperty
        );
        return problemDetails;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining("; "));
        return getProblemDetails(HttpStatus.BAD_REQUEST, "Validation Failed", detail, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleMalformed(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return getProblemDetails(HttpStatus.BAD_REQUEST, "Invalid Request", "Provide a valid JSON request body", request);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ProblemDetail handleDuplicate(DuplicateKeyException ex, HttpServletRequest request) {
        return getProblemDetails(HttpStatus.CONFLICT, "Workflow Already Exists", "This reference ID already exists", request);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error on {}", request.getRequestURI(), ex);
        return getProblemDetails(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred", request);
    }
}
