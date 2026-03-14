package com.example.backend.operation.controller;

import com.example.backend.operation.dto.AdjustmentRequest;
import com.example.backend.operation.dto.OperationResponse;
import com.example.backend.operation.entity.OperationStatus;
import com.example.backend.operation.entity.OperationType;
import com.example.backend.operation.service.OperationService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/adjustments")

public class AdjustmentController {

    private final OperationService operationService;

    public AdjustmentController(OperationService operationService) {
        this.operationService = operationService;
    }

    @GetMapping
    public ResponseEntity<List<OperationResponse>> getAll(
            @RequestParam(required = false) OperationStatus status) {
        return ResponseEntity.ok(operationService.getAll(OperationType.ADJUSTMENT, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperationResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(operationService.getById(id));
    }

    @PostMapping
    public ResponseEntity<OperationResponse> create(@Valid @RequestBody AdjustmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(operationService.createAdjustment(request));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<OperationResponse> validate(@PathVariable Long id) {
        return ResponseEntity.ok(operationService.validateAdjustment(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OperationResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(operationService.cancelOperation(id));
    }
}
