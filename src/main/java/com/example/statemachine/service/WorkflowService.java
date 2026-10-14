package com.example.statemachine.service;

import com.example.statemachine.dto.WorkflowRequest;
import com.example.statemachine.model.WorkflowInstance;

public interface WorkflowService {

    WorkflowInstance verifyOtp(WorkflowRequest request);
    WorkflowInstance submitProfile(WorkflowRequest request);
    WorkflowInstance enterAccountStatement(WorkflowRequest request);
    WorkflowInstance uploadUdyamDocument(WorkflowRequest request);
}