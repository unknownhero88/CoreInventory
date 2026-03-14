package com.example.backend.operation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ReceiptRequest {
    private String supplierName;

    @NotNull(message = "Destination location is required")
    private Long toLocationId;

    private String notes;

    @NotEmpty(message = "At least one product line is required")
    @Valid
    private List<OperationLineRequest> lines;

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Long getToLocationId() {
        return toLocationId;
    }

    public void setToLocationId(Long toLocationId) {
        this.toLocationId = toLocationId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<OperationLineRequest> getLines() {
        return lines;
    }

    public void setLines(List<OperationLineRequest> lines) {
        this.lines = lines;
    }
}
