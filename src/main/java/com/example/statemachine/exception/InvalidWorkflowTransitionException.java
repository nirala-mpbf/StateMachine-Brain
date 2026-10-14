package com.example.statemachine.exception;

import com.example.statemachine.statemachine.event.WorkflowEvent;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class InvalidWorkflowTransitionException extends ApiException {

    private final List<WorkflowEvent> allowedEvents;

    public InvalidWorkflowTransitionException(String message, List<WorkflowEvent> allowedEvents) {
        super(message);
        this.allowedEvents = allowedEvents;
    }

    public HttpStatus status() {
        return HttpStatus.CONFLICT;
    }

    public String title() {
        return "Invalid Workflow Transition";
    }

    public Map<String, Object> properties() {
        return Map.of("allowedEvents", allowedEvents);
    }
}
