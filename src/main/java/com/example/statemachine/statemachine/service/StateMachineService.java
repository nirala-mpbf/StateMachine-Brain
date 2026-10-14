package com.example.statemachine.statemachine.service;

import com.example.statemachine.model.WorkflowInstance;
import com.example.statemachine.statemachine.event.WorkflowEvent;

public interface StateMachineService {

    WorkflowInstance createWorkflow(String referenceId);

    WorkflowInstance getWorkflow(String referenceId);

    WorkflowInstance processEvent(String referenceId, WorkflowEvent event);
}