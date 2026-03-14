package com.example.backend.operation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class TransferRequest {
    @NotNull(message = "Source location is required")
    private Long fromLocationId;

    @NotNull(message = "Destination location is required")
    private Long toLocationId;

    private String notes;

    @NotEmpty(message = "At least one product line is required")
    @Valid
    private List<OperationLineRequest> lines;

    public Long getFromLocationId() {
        return fromLocationId;
    }

    public void setFromLocationId(Long fromLocationId) {
        this.fromLocationId = fromLocationId;
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
