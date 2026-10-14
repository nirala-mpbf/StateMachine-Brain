package com.example.statemachine.exception;

import org.springframework.http.HttpStatus;

public class WorkflowConflictException extends ApiException {

    public WorkflowConflictException(String message) {
        super(message);
    }

    public HttpStatus status() {
        return HttpStatus.CONFLICT;
    }

    public String title() {
        return "Workflow Conflict";
    }
}
