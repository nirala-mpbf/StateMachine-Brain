package com.example.statemachine.dto;

import com.example.statemachine.statemachine.event.WorkflowEvent;
import com.example.statemachine.statemachine.state.WorkflowState;

public record Transition(WorkflowState source,
                         WorkflowEvent event,
                         WorkflowState target) {
}
