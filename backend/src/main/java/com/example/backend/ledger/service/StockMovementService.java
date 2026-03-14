package com.example.backend.ledger.service;

import com.example.backend.ledger.entity.StockMovement;
import com.example.backend.ledger.repository.StockMovementRepository;

import com.example.backend.operation.entity.Operation;
import com.example.backend.operation.entity.OperationType;
import com.example.backend.product.entity.Product;
import com.example.backend.warehouse.entity.WarehouseLocation;
import org.springframework.stereotype.Service;

@Service

public class StockMovementService {

    private final StockMovementRepository movementRepository;

    public StockMovementService(StockMovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    public StockMovement record(
            Operation operation,
            Product product,
            OperationType type,
            WarehouseLocation fromLocation,
            WarehouseLocation toLocation,
            int quantity) {

        StockMovement movement = StockMovement.builder()
                .operation(operation)
                .product(product)
                .type(type)
                .fromLocation(fromLocation)
                .toLocation(toLocation)
                .quantity(quantity)
                .build();

        return movementRepository.save(movement);
    }
}

