package com.example.backend.operation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AdjustmentLineRequest {
    @NotNull
    private Long productId;

    @Min(value = 0)
    private int countedQty;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getCountedQty() {
        return countedQty;
    }

    public void setCountedQty(int countedQty) {
        this.countedQty = countedQty;
    }
}
