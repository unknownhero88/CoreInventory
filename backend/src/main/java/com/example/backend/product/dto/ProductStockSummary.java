package com.example.backend.product.dto;

import java.util.List;

public class ProductStockSummary {
    private Long productId;
    private String productName;
    private String sku;
    private String unitOfMeasure;
    private int totalQuantity;
    private int reorderLevel;
    private boolean lowStock;
    private List<StockByLocationResponse> stockByLocation;

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

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public boolean isLowStock() {
        return lowStock;
    }

    public void setLowStock(boolean lowStock) {
        this.lowStock = lowStock;
    }

    public List<StockByLocationResponse> getStockByLocation() {
        return stockByLocation;
    }

    public void setStockByLocation(List<StockByLocationResponse> stockByLocation) {
        this.stockByLocation = stockByLocation;
    }
}
