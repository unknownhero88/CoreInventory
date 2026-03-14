package com.example.backend.operation.controller;

import com.example.backend.operation.dto.DeliveryRequest;
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
@RequestMapping("/api/v1/deliveries")

public class DeliveryController {

    private final OperationService operationService;

    public DeliveryController(OperationService operationService) {
        this.operationService = operationService;
    }

    @GetMapping
    public ResponseEntity<List<OperationResponse>> getAll(
            @RequestParam(required = false) OperationStatus status) {
        return ResponseEntity.ok(operationService.getAll(OperationType.DELIVERY, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperationResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(operationService.getById(id));
    }

    @PostMapping
    public ResponseEntity<OperationResponse> create(@Valid @RequestBody DeliveryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(operationService.createDelivery(request));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<OperationResponse> validate(@PathVariable Long id) {
        return ResponseEntity.ok(operationService.validateDelivery(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OperationResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(operationService.cancelOperation(id));
    }
}

