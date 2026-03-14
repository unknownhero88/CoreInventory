package com.example.backend.operation.entity;


import com.example.backend.product.entity.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "operation_lines")
public class OperationLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id", nullable = false)
    private Operation operation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "expected_qty", nullable = false)
    private int expectedQty;

    @Column(name = "done_qty")
    private int doneQty = 0;

    public OperationLine() {
    }

    public OperationLine(Long id, Operation operation, Product product, int expectedQty, int doneQty) {
        this.id = id;
        this.operation = operation;
        this.product = product;
        this.expectedQty = expectedQty;
        this.doneQty = doneQty;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public Operation getOperation() {
        return operation;
    }

    public Product getProduct() {
        return product;
    }

    public int getExpectedQty() {
        return expectedQty;
    }

    public int getDoneQty() {
        return doneQty;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setExpectedQty(int expectedQty) {
        this.expectedQty = expectedQty;
    }

    public void setDoneQty(int doneQty) {
        this.doneQty = doneQty;
    }

    // Builder

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long id;
        private Operation operation;
        private Product product;
        private int expectedQty;
        private int doneQty;

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

        public Builder expectedQty(int expectedQty) {
            this.expectedQty = expectedQty;
            return this;
        }

        public Builder doneQty(int doneQty) {
            this.doneQty = doneQty;
            return this;
        }

        public OperationLine build() {
            return new OperationLine(id, operation, product, expectedQty, doneQty);
        }
    }
}