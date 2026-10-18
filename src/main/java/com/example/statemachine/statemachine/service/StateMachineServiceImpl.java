package com.example.statemachine.statemachine.service;

import com.example.statemachine.exception.InvalidWorkflowTransitionException;
import com.example.statemachine.exception.NotFoundException;
import com.example.statemachine.exception.WorkflowConflictException;
import com.example.statemachine.model.WorkflowInstance;
import com.example.statemachine.repository.WorkflowRepository;
import com.example.statemachine.statemachine.event.WorkflowEvent;
import com.example.statemachine.statemachine.properties.WorkflowProperties;
import com.example.statemachine.statemachine.state.WorkflowState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class StateMachineServiceImpl implements  StateMachineService {

    private final StateMachineFactory<WorkflowState, WorkflowEvent> factory;
    private final WorkflowRepository repository;
    private final WorkflowProperties workflowProperties;

    public StateMachineServiceImpl(StateMachineFactory<WorkflowState, WorkflowEvent> factory,
                                   WorkflowRepository repository,
                                   WorkflowProperties workflowProperties) {
        this.factory = factory;
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
    public WorkflowInstance processEvent(String referenceId, WorkflowEvent event) {
        WorkflowInstance current = getWorkflow(referenceId);
        StateMachine<WorkflowState, WorkflowEvent> statemachine = factory.getStateMachine("WORKFLOW_" + referenceId);

        try {
            statemachine.stop();

            statemachine
                    .getStateMachineAccessor()
                    .doWithAllRegions( accessor -> accessor.resetStateMachine( new DefaultStateMachineContext<>( current.state(), null, null, null)));

            statemachine.start();

            if (!statemachine.sendEvent(event)) {
                List<WorkflowEvent> allowed = statemachine.getTransitions().stream()
                        .filter(t -> t.getSource().getId() == current.state())
                        .map(t -> t.getTrigger() == null ? null : t.getTrigger().getEvent())
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList();

                throw new InvalidWorkflowTransitionException("Event " + event + " is not allowed in state " + current.state(), allowed);
            }
            WorkflowState nextState = statemachine.getState().getId();

            boolean updateStatus = repository.updateState(referenceId, current.state(), nextState);
            if (!updateStatus) {
                throw new WorkflowConflictException("Workflow state was changed by another request. Please retry.");
            }
            return getWorkflow(referenceId);

        } finally {
            statemachine.stop();
        }
    }
}
