package com.example.statemachine.controller;

import com.example.statemachine.dto.WorkflowRequest;
import com.example.statemachine.model.WorkflowInstance;
import com.example.statemachine.service.WorkflowService;
import com.example.statemachine.statemachine.service.StateMachineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workflows")
public class WorkflowController {

    private final WorkflowService workflowService;
    private final StateMachineService stateMachineService;

    public WorkflowController(WorkflowService workflowService, StateMachineService stateMachineService) {
        this.workflowService = workflowService;
        this.stateMachineService = stateMachineService;
    }

    @PostMapping("/create")
    public ResponseEntity<WorkflowInstance> createWorkflow(@RequestBody WorkflowRequest request) {

        WorkflowInstance workflow = stateMachineService.createWorkflow(request.referenceId());
        return new ResponseEntity<>(workflow, HttpStatus.CREATED);
    }

    @PostMapping("/get")
    public ResponseEntity<WorkflowInstance> getWorkflow(@RequestBody WorkflowRequest request) {

        WorkflowInstance workflow = stateMachineService.getWorkflow(request.referenceId());
        return new ResponseEntity<>(workflow, HttpStatus.OK);
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<WorkflowInstance> verifyOtp(@RequestBody WorkflowRequest request) {

        WorkflowInstance workflow = workflowService.verifyOtp(request);
        return new ResponseEntity<>(workflow, HttpStatus.OK);
    }

    @PostMapping("/submit-profile")
    public ResponseEntity<WorkflowInstance> submitProfile(@RequestBody WorkflowRequest request) {

        WorkflowInstance workflow = workflowService.submitProfile(request);
        return new ResponseEntity<>(workflow, HttpStatus.OK);
    }

    @PostMapping("/enter-account-statement")
    public ResponseEntity<WorkflowInstance> enterAccountStatement(@RequestBody WorkflowRequest request) {

        WorkflowInstance workflow = workflowService.enterAccountStatement(request);
        return new ResponseEntity<>(workflow, HttpStatus.OK);
    }

    @PostMapping("/udyam-document")
    public ResponseEntity<WorkflowInstance> uploadUdyamDocument(@RequestBody WorkflowRequest request) {

        WorkflowInstance workflow = workflowService.uploadUdyamDocument(request);
        return new ResponseEntity<>(workflow, HttpStatus.OK);
    }
}