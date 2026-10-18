package com.example.statemachine.statemachine.properties;

import com.example.statemachine.dto.Transition;
import com.example.statemachine.exception.InvalidWorkflowConfigurationException;
import com.example.statemachine.statemachine.event.WorkflowEvent;
import com.example.statemachine.statemachine.state.WorkflowState;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "brain.workflow", ignoreUnknownFields = false)
public record WorkflowProperties(WorkflowState initialState,
                                 WorkflowState terminalState,
                                 List<WorkflowState> states,
                                 List<Transition> transitions) {

    public WorkflowProperties {

        if (initialState == null || terminalState == null || states == null || states.isEmpty() || transitions == null || transitions.isEmpty()) {
            throw new InvalidWorkflowConfigurationException("Workflow configuration is incomplete");
        }

        WorkflowState[] expectedStates = WorkflowState.values();
        WorkflowEvent[] expectedEvents = WorkflowEvent.values();

        // Validate states against enum declaration order.
        if (!states.equals(List.of(expectedStates))) {
            throw new InvalidWorkflowConfigurationException("State order does not match WorkflowState enum");
        }

        if (initialState != expectedStates[0] || terminalState != expectedStates[expectedStates.length - 1]) {
            throw new InvalidWorkflowConfigurationException("Invalid initial or terminal state");
        }

        // Linear workflow requires one transition per non-terminal state.
        if (transitions.size() != expectedStates.length - 1 || transitions.size() != expectedEvents.length) {
            throw new InvalidWorkflowConfigurationException("Invalid number of workflow transitions");
        }

        // Validate transition sequence and events.
        for (int i = 0; i < transitions.size(); i++) {

            Transition transition = transitions.get(i);

            if (transition == null || transition.source() == null || transition.target() == null || transition.event() == null) {
                throw new InvalidWorkflowConfigurationException("Transition " + (i + 1) + " is incomplete");
            }

            if (transition.source() != expectedStates[i] || transition.target() != expectedStates[i + 1]) {
                throw new InvalidWorkflowConfigurationException("Invalid transition at " + expectedStates[i]);
            }

            if (transition.event() != expectedEvents[i]) {
                throw new InvalidWorkflowConfigurationException("Invalid event at " + expectedStates[i]);
            }
        }

        states = List.copyOf(states);
        transitions = List.copyOf(transitions);
    }
}
