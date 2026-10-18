package com.example.statemachine.service.impl;

import com.example.statemachine.dto.WorkflowRequest;
import com.example.statemachine.exception.NotFoundException;
import com.example.statemachine.exception.WorkflowConflictException;
import com.example.statemachine.model.WorkflowInstance;
import com.example.statemachine.repository.WorkflowRepository;
import com.example.statemachine.service.WorkflowService;
import com.example.statemachine.statemachine.event.WorkflowEvent;
import com.example.statemachine.statemachine.properties.WorkflowProperties;
import com.example.statemachine.statemachine.service.StateMachineService;
import com.example.statemachine.statemachine.state.WorkflowState;
import org.springframework.stereotype.Service;

@Service
public class WorkflowServiceImpl implements WorkflowService {
    private final StateMachineService stateMachineService;
    private final WorkflowRepository repository;
    private final WorkflowProperties workflowProperties;

    public WorkflowServiceImpl(StateMachineService stateMachineService,
                               WorkflowRepository repository,
                               WorkflowProperties workflowProperties) {
        this.stateMachineService = stateMachineService;
        this.repository = repository;
        this.workflowProperties = workflowProperties;
    }

    @Override
    public WorkflowInstance createWorkflow(String referenceId) {
        repository.create(referenceId, workflowProperties.initialState());
        return getWorkflow(referenceId);
    }

    @Override
    public WorkflowInstance getWorkflow(String referenceId) {
        return repository.findByReferenceId(referenceId)
                .orElseThrow(() -> new NotFoundException("Workflow " + referenceId + " not found"));
    }

    @Override
    public WorkflowInstance verifyOtp(WorkflowRequest request) {
        return processEvent(request.referenceId(), WorkflowEvent.OTP_VERIFIED);
    }

    @Override
    public WorkflowInstance submitProfile(WorkflowRequest request) {
        return processEvent(request.referenceId(), WorkflowEvent.PROFILE_SUBMITTED);
    }

    @Override
    public WorkflowInstance enterAccountStatement(WorkflowRequest request) {
        return processEvent(request.referenceId(), WorkflowEvent.ACCOUNT_STATEMENT_UPLOADED);
    }

    @Override
    public WorkflowInstance uploadUdyamDocument(WorkflowRequest request) {
        return processEvent(request.referenceId(), WorkflowEvent.UDYAM_DOCUMENT_UPLOADED);
    }

    private WorkflowInstance processEvent(String referenceId, WorkflowEvent event) {
        WorkflowInstance current = getWorkflow(referenceId);
        WorkflowState nextState = stateMachineService.transition(referenceId, current.state(), event);

        if (!repository.updateState(referenceId, current.state(), nextState)) {
            throw new WorkflowConflictException("Workflow state was changed by another request. Please retry.");
        }
        return getWorkflow(referenceId);
    }
}
