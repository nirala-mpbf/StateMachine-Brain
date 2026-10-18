package com.example.statemachine.statemachine.config;

import com.example.statemachine.dto.Transition;
import com.example.statemachine.statemachine.event.WorkflowEvent;
import com.example.statemachine.statemachine.properties.WorkflowProperties;
import com.example.statemachine.statemachine.state.WorkflowState;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
@EnableConfigurationProperties(WorkflowProperties.class)
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<WorkflowState, WorkflowEvent> {

    private final WorkflowProperties workflow;

    public StateMachineConfig(WorkflowProperties workflow) {
        this.workflow = workflow;
    }

    @Override
    public void configure(StateMachineStateConfigurer<WorkflowState, WorkflowEvent> states) throws Exception {
        states.withStates()
                    .initial(workflow.initialState())
                    .states(EnumSet.copyOf(workflow.states()))
                    .end(workflow.terminalState());
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<WorkflowState, WorkflowEvent> transitions)
            throws Exception {

        for (Transition transition : workflow.transitions()) {

            transitions.withExternal()
                    .source(transition.source())
                    .target(transition.target())
                    .event(transition.event());
        }
    }
}
