package com.example.statemachine.controller;

import com.example.statemachine.dto.WorkflowRequest;
import com.example.statemachine.model.WorkflowInstance;
import com.example.statemachine.service.WorkflowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workflows")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping("/create")
    public ResponseEntity<WorkflowInstance> createWorkflow(@RequestBody WorkflowRequest request) {

        WorkflowInstance workflow = workflowService.createWorkflow(request.referenceId());
        return new ResponseEntity<>(workflow, HttpStatus.CREATED);
    }

    @PostMapping("/get")
    public ResponseEntity<WorkflowInstance> getWorkflow(@RequestBody WorkflowRequest request) {

        WorkflowInstance workflow = workflowService.getWorkflow(request.referenceId());
        return ResponseEntity.ok(workflow);
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<WorkflowInstance> verifyOtp(@RequestBody WorkflowRequest request) {

        WorkflowInstance workflow = workflowService.verifyOtp(request);
        return ResponseEntity.ok(workflow);
    }

    @PostMapping("/submit-profile")
    public ResponseEntity<WorkflowInstance> submitProfile(@RequestBody WorkflowRequest request) {

        WorkflowInstance workflow = workflowService.submitProfile(request);
        return ResponseEntity.ok(workflow);
    }

    @PostMapping("/enter-account-statement")
    public ResponseEntity<WorkflowInstance> enterAccountStatement(@RequestBody WorkflowRequest request) {

        WorkflowInstance workflow = workflowService.enterAccountStatement(request);
        return ResponseEntity.ok(workflow);
    }

    @PostMapping("/udyam-document")
    public ResponseEntity<WorkflowInstance> uploadUdyamDocument(@RequestBody WorkflowRequest request) {

        WorkflowInstance workflow = workflowService.uploadUdyamDocument(request);
        return ResponseEntity.ok(workflow);
    }
}
