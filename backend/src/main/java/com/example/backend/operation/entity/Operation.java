package com.example.backend.operation.entity;


import com.example.backend.user.entity.User;
import com.example.backend.warehouse.entity.WarehouseLocation;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "operations")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "supplier_name")
    private String supplierName;

    @Column(name = "customer_ref")
    private String customerRef;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_location_id")
    private WarehouseLocation toLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_location_id")
    private WarehouseLocation fromLocation;

    @Column
    private String notes;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "operation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperationLine> lines = new ArrayList<>();

    public Operation() {
    }

    public Operation(Long id, String reference, OperationType type, OperationStatus status,
                     User createdBy, String supplierName, String customerRef,
                     WarehouseLocation toLocation, WarehouseLocation fromLocation,
                     String notes, LocalDateTime validatedAt, LocalDateTime createdAt,
                     List<OperationLine> lines) {

        this.id = id;
        this.reference = reference;
        this.type = type;
        this.status = status;
        this.createdBy = createdBy;
        this.supplierName = supplierName;
        this.customerRef = customerRef;
        this.toLocation = toLocation;
        this.fromLocation = fromLocation;
        this.notes = notes;
        this.validatedAt = validatedAt;
        this.createdAt = createdAt;
        this.lines = lines;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = OperationStatus.DRAFT;
        }
    }

    // Getters and Setters

    public Long getId() { return id; }

    public String getReference() { return reference; }

    public OperationType getType() { return type; }

    public OperationStatus getStatus() { return status; }

    public User getCreatedBy() { return createdBy; }

    public String getSupplierName() { return supplierName; }

    public String getCustomerRef() { return customerRef; }

    public WarehouseLocation getToLocation() { return toLocation; }

    public WarehouseLocation getFromLocation() { return fromLocation; }

    public String getNotes() { return notes; }

    public LocalDateTime getValidatedAt() { return validatedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<OperationLine> getLines() { return lines; }

    public void setId(Long id) { this.id = id; }

    public void setReference(String reference) { this.reference = reference; }

    public void setType(OperationType type) { this.type = type; }

    public void setStatus(OperationStatus status) { this.status = status; }

    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public void setCustomerRef(String customerRef) { this.customerRef = customerRef; }

    public void setToLocation(WarehouseLocation toLocation) { this.toLocation = toLocation; }

    public void setFromLocation(WarehouseLocation fromLocation) { this.fromLocation = fromLocation; }

    public void setNotes(String notes) { this.notes = notes; }

    public void setValidatedAt(LocalDateTime validatedAt) { this.validatedAt = validatedAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void setLines(List<OperationLine> lines) { this.lines = lines; }

    // Builder

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long id;
        private String reference;
        private OperationType type;
        private OperationStatus status;
        private User createdBy;
        private String supplierName;
        private String customerRef;
        private WarehouseLocation toLocation;
        private WarehouseLocation fromLocation;
        private String notes;
        private LocalDateTime validatedAt;
        private LocalDateTime createdAt;
        private List<OperationLine> lines = new ArrayList<>();

        public Builder id(Long id) { this.id = id; return this; }

        public Builder reference(String reference) { this.reference = reference; return this; }

        public Builder type(OperationType type) { this.type = type; return this; }

        public Builder status(OperationStatus status) { this.status = status; return this; }

        public Builder createdBy(User createdBy) { this.createdBy = createdBy; return this; }

        public Builder supplierName(String supplierName) { this.supplierName = supplierName; return this; }

        public Builder customerRef(String customerRef) { this.customerRef = customerRef; return this; }

        public Builder toLocation(WarehouseLocation toLocation) { this.toLocation = toLocation; return this; }

        public Builder fromLocation(WarehouseLocation fromLocation) { this.fromLocation = fromLocation; return this; }

        public Builder notes(String notes) { this.notes = notes; return this; }

        public Builder validatedAt(LocalDateTime validatedAt) { this.validatedAt = validatedAt; return this; }

        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Builder lines(List<OperationLine> lines) { this.lines = lines; return this; }

        public Operation build() {
            return new Operation(id, reference, type, status, createdBy, supplierName,
                    customerRef, toLocation, fromLocation, notes,
                    validatedAt, createdAt, lines);
        }
    }
}