package com.example.statemachine.model;
import com.example.statemachine.statemachine.state.WorkflowState;

public record WorkflowInstance(
        String referenceId,
        WorkflowState state
) {}
