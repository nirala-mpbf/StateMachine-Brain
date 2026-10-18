package com.example.statemachine.statemachine.service;

import com.example.statemachine.statemachine.event.WorkflowEvent;
import com.example.statemachine.statemachine.state.WorkflowState;

public interface StateMachineService {
    WorkflowState transition(String referenceId, WorkflowState currentState, WorkflowEvent event);
}
