package com.example.statemachine.service.impl;

import com.example.statemachine.dto.WorkflowRequest;
import com.example.statemachine.exception.NotFoundException;
import com.example.statemachine.exception.WorkflowConflictException;
import com.example.statemachine.dto.WorkflowDetails;
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
    public WorkflowDetails createWorkflow(String referenceId) {
        repository.create(referenceId, workflowProperties.initialState());
        return getWorkflow(referenceId);
    }

    @Override
    public WorkflowDetails getWorkflow(String referenceId) {
        WorkflowDetails details = findWorkflow(referenceId);
        populateWorkflowDetails(details);
        return details;
    }

    @Override
    public WorkflowDetails verifyOtp(WorkflowRequest request) {
        return processEvent(request.referenceId(), WorkflowEvent.OTP_VERIFIED);
    }

    @Override
    public WorkflowDetails submitProfile(WorkflowRequest request) {
        return processEvent(request.referenceId(), WorkflowEvent.PROFILE_SUBMITTED);
    }

    @Override
    public WorkflowDetails enterAccountStatement(WorkflowRequest request) {
        return processEvent(request.referenceId(), WorkflowEvent.ACCOUNT_STATEMENT_UPLOADED);
    }

    @Override
    public WorkflowDetails uploadUdyamDocument(WorkflowRequest request) {
        return processEvent(request.referenceId(), WorkflowEvent.UDYAM_DOCUMENT_UPLOADED);
    }

    private WorkflowDetails processEvent(String referenceId, WorkflowEvent event) {
        WorkflowDetails current = findWorkflow(referenceId);
        WorkflowState nextState = stateMachineService.transition(referenceId, current.getCurrentState(), event);

        if (!repository.updateState(referenceId, current.getCurrentState(), nextState)) {
            throw new WorkflowConflictException("Workflow state was changed by another request. Please retry.");
        }
        return getWorkflow(referenceId);
    }

    private WorkflowDetails findWorkflow(String referenceId) {
        return repository.findByReferenceId(referenceId)
                .orElseThrow(() -> new NotFoundException("Workflow " + referenceId + " not found"));
    }

    private void populateWorkflowDetails(WorkflowDetails workflowDetails) {

        workflowDetails.setCompleted( workflowDetails.getCurrentState() == workflowProperties.terminalState());
        workflowDetails.setNextEvent(null);
        workflowDetails.setNextState(null);
        workflowProperties
                .transitions()
                .stream()
                .filter( transition -> transition.source() == workflowDetails.getCurrentState())
                .findFirst()
                .ifPresent( transition -> {
                    workflowDetails.setNextEvent(transition.event());
                    workflowDetails.setNextState(transition.target());
                });
    }
}
