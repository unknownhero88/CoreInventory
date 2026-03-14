package com.example.backend.ledger.controller;

import com.example.backend.ledger.dto.MovementResponse;
import com.example.backend.ledger.entity.StockMovement;
import com.example.backend.ledger.repository.StockMovementRepository;
import com.example.backend.operation.entity.OperationType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/movements")

public class MovementController {

    private final StockMovementRepository movementRepository;

    public MovementController(StockMovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    @GetMapping
    public ResponseEntity<List<MovementResponse>> getAll(
            @RequestParam(required = false) OperationType type,
            @RequestParam(required = false) Long productId) {

        List<StockMovement> movements;

        if (productId != null) {
            movements = movementRepository.findByProductIdOrderByMovedAtDesc(productId);
        } else if (type != null) {
            movements = movementRepository.findByTypeOrderByMovedAtDesc(type);
        } else {
            movements = movementRepository.findAllByOrderByMovedAtDesc();
        }

        return ResponseEntity.ok(movements.stream().map(this::toResponse).collect(Collectors.toList()));
    }

    private MovementResponse toResponse(StockMovement m) {
        MovementResponse res = new MovementResponse();
        res.setId(m.getId());
        res.setType(m.getType());
        res.setQuantity(m.getQuantity());
        res.setMovedAt(m.getMovedAt());

        if (m.getOperation() != null) res.setOperationReference(m.getOperation().getReference());
        if (m.getProduct()   != null) {
            res.setProductName(m.getProduct().getName());
            res.setSku(m.getProduct().getSku());
        }
        if (m.getFromLocation() != null)
            res.setFromLocation(m.getFromLocation().getWarehouse().getName() + " / " + m.getFromLocation().getName());
        if (m.getToLocation()   != null)
            res.setToLocation(m.getToLocation().getWarehouse().getName() + " / " + m.getToLocation().getName());

        return res;
    }
}
