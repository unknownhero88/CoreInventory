package com.example.backend.product.entity;


import com.example.backend.warehouse.entity.WarehouseLocation;
import jakarta.persistence.*;

@Entity
@Table(
        name = "product_stock",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "location_id"})
)
public class ProductStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private WarehouseLocation location;

    @Column(nullable = false)
    private int quantity = 0;

    public ProductStock() {
    }

    public ProductStock(Long id, Product product, WarehouseLocation location, int quantity) {
        this.id = id;
        this.product = product;
        this.location = location;
        this.quantity = quantity;
    }

    // Business Methods

    public void addQuantity(int qty) {
        this.quantity += qty;
    }

    public void subtractQuantity(int qty) {
        if (this.quantity < qty) {
            throw new IllegalStateException(
                    "Insufficient stock at location. Available: " + this.quantity + ", Requested: " + qty
            );
        }
        this.quantity -= qty;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public WarehouseLocation getLocation() {
        return location;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setLocation(WarehouseLocation location) {
        this.location = location;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Builder

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long id;
        private Product product;
        private WarehouseLocation location;
        private int quantity;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder product(Product product) {
            this.product = product;
            return this;
        }

        public Builder location(WarehouseLocation location) {
            this.location = location;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public ProductStock build() {
            return new ProductStock(id, product, location, quantity);
        }
    }
}
