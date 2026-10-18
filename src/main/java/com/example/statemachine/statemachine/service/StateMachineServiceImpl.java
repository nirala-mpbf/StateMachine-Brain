package com.example.statemachine.statemachine.service;

import com.example.statemachine.exception.InvalidWorkflowTransitionException;
import com.example.statemachine.statemachine.event.WorkflowEvent;
import com.example.statemachine.statemachine.state.WorkflowState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class StateMachineServiceImpl implements StateMachineService {

    private final StateMachineFactory<WorkflowState, WorkflowEvent> factory;

    public StateMachineServiceImpl(StateMachineFactory<WorkflowState, WorkflowEvent> factory) {
        this.factory = factory;
    }

    @Override
    public WorkflowState transition(String referenceId, WorkflowState currentState, WorkflowEvent event) {
        StateMachine<WorkflowState, WorkflowEvent> statemachine = factory.getStateMachine("WORKFLOW_" + referenceId);

        try {
            statemachine.stop();

            statemachine
                    .getStateMachineAccessor()
                    .doWithAllRegions(accessor -> accessor.resetStateMachine(
                            new DefaultStateMachineContext<>(currentState, null, null, null)));

            statemachine.start();

            if (!statemachine.sendEvent(event)) {
                throw new InvalidWorkflowTransitionException(
                        "Event " + event + " is not allowed in state " + currentState,
                        getAllowedEvents(statemachine, currentState));
            }
            return statemachine.getState().getId();

        } finally {
            statemachine.stop();
        }
    }

    private List<WorkflowEvent> getAllowedEvents(
            StateMachine<WorkflowState, WorkflowEvent> stateMachine,
            WorkflowState currentState) {
        return stateMachine.getTransitions().stream()
                .filter(transition -> transition.getSource().getId() == currentState)
                .map(transition -> transition.getTrigger() == null
                        ? null
                        : transition.getTrigger().getEvent())
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }
}
