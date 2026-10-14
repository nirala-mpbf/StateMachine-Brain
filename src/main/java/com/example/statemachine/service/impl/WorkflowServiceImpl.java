package com.example.statemachine.service.impl;

import com.example.statemachine.dto.WorkflowRequest;
import com.example.statemachine.model.WorkflowInstance;
import com.example.statemachine.service.WorkflowService;
import com.example.statemachine.statemachine.event.WorkflowEvent;
import com.example.statemachine.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    private final StateMachineService stateMachineService;

    public WorkflowServiceImpl(StateMachineService stateMachineService) {
        this.stateMachineService = stateMachineService;
    }

    @Override
    public WorkflowInstance verifyOtp(WorkflowRequest request) {

        // OTP verification logic.

        WorkflowInstance workflow = stateMachineService.processEvent(
                request.referenceId(),
                WorkflowEvent.OTP_VERIFIED
        );

        return workflow;
    }

    @Override
    public WorkflowInstance submitProfile(WorkflowRequest request) {

        //  profile submission logic

        WorkflowInstance workflow = stateMachineService.processEvent(
                request.referenceId(),
                WorkflowEvent.PROFILE_SUBMITTED
        );

        return workflow;
    }

    @Override
    public WorkflowInstance enterAccountStatement(WorkflowRequest request) {

        //  account statement logic.

        WorkflowInstance workflow = stateMachineService.processEvent(
                request.referenceId(),
                WorkflowEvent.ACCOUNT_STATEMENT_UPLOADED
        );

        return workflow;
    }

    @Override
    public WorkflowInstance uploadUdyamDocument(WorkflowRequest request) {

        // Udyam document verification + logic.

        WorkflowInstance workflow = stateMachineService.processEvent(
                request.referenceId(),
                WorkflowEvent.UDYAM_DOCUMENT_UPLOADED
        );

        return workflow;
    }
}