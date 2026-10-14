package com.example.statemachine.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {

    public NotFoundException(String message) {
        super(message);
    }

    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }

    public String title() {
        return "Workflow Not Found";
    }
}
