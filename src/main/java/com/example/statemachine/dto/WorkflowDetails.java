package com.example.statemachine.dto;

import com.example.statemachine.statemachine.event.WorkflowEvent;
import com.example.statemachine.statemachine.state.WorkflowState;


public class WorkflowDetails {
    private String  referenceId;
    private WorkflowState currentState;
    private WorkflowEvent nextEvent;
    private WorkflowState nextState;
    private boolean isCompleted;

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public WorkflowState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(WorkflowState currentState) {
        this.currentState = currentState;
    }

    public WorkflowEvent getNextEvent() {
        return nextEvent;
    }

    public void setNextEvent(WorkflowEvent nextEvent) {
        this.nextEvent = nextEvent;
    }

    public WorkflowState getNextState() {
        return nextState;
    }

    public void setNextState(WorkflowState nextState) {
        this.nextState = nextState;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
