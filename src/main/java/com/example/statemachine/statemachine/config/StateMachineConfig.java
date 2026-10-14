package com.example.statemachine.statemachine.config;

import com.example.statemachine.statemachine.event.WorkflowEvent;
import com.example.statemachine.statemachine.state.WorkflowState;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<WorkflowState, WorkflowEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<WorkflowState, WorkflowEvent> states) throws Exception {
        states.withStates()
                .initial(WorkflowState.OTP_VERIFICATION)
                .states(EnumSet.allOf(WorkflowState.class))
                .end(WorkflowState.COMPLETED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<WorkflowState, WorkflowEvent> transitions)
            throws Exception {

        transitions.
                withExternal()
                .source(WorkflowState.OTP_VERIFICATION)
                .target(WorkflowState.PROFILE_DETAILS)
                .event(WorkflowEvent.OTP_VERIFIED)

                .and()
                .withExternal()
                .source(WorkflowState.PROFILE_DETAILS)
                .target(WorkflowState.ACCOUNT_STATEMENT_UPLOAD)
                .event(WorkflowEvent.PROFILE_SUBMITTED)

                .and()
                .withExternal()
                .source(WorkflowState.ACCOUNT_STATEMENT_UPLOAD)
                .target(WorkflowState.UDYAM_DOCUMENT_UPLOAD)
                .event(WorkflowEvent.ACCOUNT_STATEMENT_UPLOADED)

                .and()
                .withExternal()
                .source(WorkflowState.UDYAM_DOCUMENT_UPLOAD)
                .target(WorkflowState.COMPLETED)
                .event(WorkflowEvent.UDYAM_DOCUMENT_UPLOADED);
    }
}
