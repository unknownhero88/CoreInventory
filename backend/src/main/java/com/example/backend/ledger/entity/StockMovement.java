package com.example.backend.ledger.entity;

import com.example.backend.operation.entity.Operation;
import com.example.backend.operation.entity.OperationType;
import com.example.backend.product.entity.Product;
import com.example.backend.warehouse.entity.WarehouseLocation;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id")
    private Operation operation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_location_id")
    private WarehouseLocation fromLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_location_id")
    private WarehouseLocation toLocation;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "moved_at", nullable = false)
    private LocalDateTime movedAt;

    public StockMovement() {
    }

    public StockMovement(Long id, Operation operation, Product product,
                         OperationType type, WarehouseLocation fromLocation,
                         WarehouseLocation toLocation, int quantity,
                         LocalDateTime movedAt) {
        this.id = id;
        this.operation = operation;
        this.product = product;
        this.type = type;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.quantity = quantity;
        this.movedAt = movedAt;
    }

    @PrePersist
    public void prePersist() {
        this.movedAt = LocalDateTime.now();
    }

    // Getters

    public Long getId() { return id; }

    public Operation getOperation() { return operation; }

    public Product getProduct() { return product; }

    public OperationType getType() { return type; }

    public WarehouseLocation getFromLocation() { return fromLocation; }

    public WarehouseLocation getToLocation() { return toLocation; }

    public int getQuantity() { return quantity; }

    public LocalDateTime getMovedAt() { return movedAt; }

    // Setters

    public void setId(Long id) { this.id = id; }

    public void setOperation(Operation operation) { this.operation = operation; }

    public void setProduct(Product product) { this.product = product; }

    public void setType(OperationType type) { this.type = type; }

    public void setFromLocation(WarehouseLocation fromLocation) { this.fromLocation = fromLocation; }

    public void setToLocation(WarehouseLocation toLocation) { this.toLocation = toLocation; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void setMovedAt(LocalDateTime movedAt) { this.movedAt = movedAt; }

    // Builder

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long id;
        private Operation operation;
        private Product product;
        private OperationType type;
        private WarehouseLocation fromLocation;
        private WarehouseLocation toLocation;
        private int quantity;
        private LocalDateTime movedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder operation(Operation operation) {
            this.operation = operation;
            return this;
        }

        public Builder product(Product product) {
            this.product = product;
            return this;
        }

        public Builder type(OperationType type) {
            this.type = type;
            return this;
        }

        public Builder fromLocation(WarehouseLocation fromLocation) {
            this.fromLocation = fromLocation;
            return this;
        }

        public Builder toLocation(WarehouseLocation toLocation) {
            this.toLocation = toLocation;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder movedAt(LocalDateTime movedAt) {
            this.movedAt = movedAt;
            return this;
        }

        public StockMovement build() {
            return new StockMovement(
                    id,
                    operation,
                    product,
                    type,
                    fromLocation,
                    toLocation,
                    quantity,
                    movedAt
            );
        }
    }
}