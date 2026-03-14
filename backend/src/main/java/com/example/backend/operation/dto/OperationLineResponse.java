package com.example.backend.operation.dto;

public class OperationLineResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String sku;
    private String unitOfMeasure;
    private int expectedQty;
    private int doneQty;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public int getExpectedQty() {
        return expectedQty;
    }

    public void setExpectedQty(int expectedQty) {
        this.expectedQty = expectedQty;
    }

    public int getDoneQty() {
        return doneQty;
    }

    public void setDoneQty(int doneQty) {
        this.doneQty = doneQty;
    }
}
