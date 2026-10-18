package com.example.statemachine.service;

import com.example.statemachine.dto.WorkflowRequest;
import com.example.statemachine.dto.WorkflowDetails;

public interface WorkflowService {

    WorkflowDetails createWorkflow(String referenceId);
    WorkflowDetails getWorkflow(String referenceId);

    WorkflowDetails verifyOtp(WorkflowRequest request);
    WorkflowDetails submitProfile(WorkflowRequest request);
    WorkflowDetails enterAccountStatement(WorkflowRequest request);
    WorkflowDetails uploadUdyamDocument(WorkflowRequest request);
}
