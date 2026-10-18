package com.example.statemachine.controller;

import com.example.statemachine.dto.WorkflowRequest;
import com.example.statemachine.dto.WorkflowDetails;
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
    public ResponseEntity<WorkflowDetails> createWorkflow(@RequestBody WorkflowRequest request) {

        WorkflowDetails workflow = workflowService.createWorkflow(request.referenceId());
        return new ResponseEntity<>(workflow, HttpStatus.CREATED);
    }

    @PostMapping("/get")
    public ResponseEntity<WorkflowDetails> getWorkflow(@RequestBody WorkflowRequest request) {

        WorkflowDetails workflow = workflowService.getWorkflow(request.referenceId());
        return ResponseEntity.ok(workflow);
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<WorkflowDetails> verifyOtp(@RequestBody WorkflowRequest request) {

        WorkflowDetails workflow = workflowService.verifyOtp(request);
        return ResponseEntity.ok(workflow);
    }

    @PostMapping("/submit-profile")
    public ResponseEntity<WorkflowDetails> submitProfile(@RequestBody WorkflowRequest request) {

        WorkflowDetails workflow = workflowService.submitProfile(request);
        return ResponseEntity.ok(workflow);
    }

    @PostMapping("/enter-account-statement")
    public ResponseEntity<WorkflowDetails> enterAccountStatement(@RequestBody WorkflowRequest request) {

        WorkflowDetails workflow = workflowService.enterAccountStatement(request);
        return ResponseEntity.ok(workflow);
    }

    @PostMapping("/udyam-document")
    public ResponseEntity<WorkflowDetails> uploadUdyamDocument(@RequestBody WorkflowRequest request) {

        WorkflowDetails workflow = workflowService.uploadUdyamDocument(request);
        return ResponseEntity.ok(workflow);
    }
}
