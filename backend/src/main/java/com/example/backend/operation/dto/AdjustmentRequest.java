package com.example.backend.operation.dto;
import jakarta.validation.Valid;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AdjustmentRequest {
    @NotNull(message = "Location is required")
    private Long locationId;

    private String notes;

    @NotEmpty(message = "At least one product line is required")
    @Valid
    private List<AdjustmentLineRequest> lines;

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<AdjustmentLineRequest> getLines() {
        return lines;
    }

    public void setLines(List<AdjustmentLineRequest> lines) {
        this.lines = lines;
    }
}
