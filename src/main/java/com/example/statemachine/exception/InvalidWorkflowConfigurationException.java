
package com.example.statemachine.exception;

public class InvalidWorkflowConfigurationException extends IllegalStateException {

    public InvalidWorkflowConfigurationException(String message) {
        super(message);
    }
}
