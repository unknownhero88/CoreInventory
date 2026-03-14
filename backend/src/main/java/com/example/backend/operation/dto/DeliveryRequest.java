package com.example.backend.operation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class DeliveryRequest {

    private String customerRef;

    @NotNull(message = "Source location is required")
    private Long fromLocationId;

    private String notes;

    @NotEmpty(message = "At least one product line is required")
    @Valid
    private List<OperationLineRequest> lines;

    public String getCustomerRef() {
        return customerRef;
    }

    public void setCustomerRef(String customerRef) {
        this.customerRef = customerRef;
    }

    public Long getFromLocationId() {
        return fromLocationId;
    }

    public void setFromLocationId(Long fromLocationId) {
        this.fromLocationId = fromLocationId;
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
