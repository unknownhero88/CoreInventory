package com.example.backend.dashboard.dto;


public class DashboardKpiResponse {

    private int totalProducts;
    private int lowStockCount;
    private int outOfStockCount;
    private long pendingReceipts;
    private long pendingDeliveries;
    private long scheduledTransfers;

    public DashboardKpiResponse() {
    }

    public DashboardKpiResponse(int totalProducts, int lowStockCount, int outOfStockCount,
                                long pendingReceipts, long pendingDeliveries, long scheduledTransfers) {
        this.totalProducts = totalProducts;
        this.lowStockCount = lowStockCount;
        this.outOfStockCount = outOfStockCount;
        this.pendingReceipts = pendingReceipts;
        this.pendingDeliveries = pendingDeliveries;
        this.scheduledTransfers = scheduledTransfers;
    }

    // Getters and Setters

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public int getLowStockCount() {
        return lowStockCount;
    }

    public void setLowStockCount(int lowStockCount) {
        this.lowStockCount = lowStockCount;
    }

    public int getOutOfStockCount() {
        return outOfStockCount;
    }

    public void setOutOfStockCount(int outOfStockCount) {
        this.outOfStockCount = outOfStockCount;
    }

    public long getPendingReceipts() {
        return pendingReceipts;
    }

    public void setPendingReceipts(long pendingReceipts) {
        this.pendingReceipts = pendingReceipts;
    }

    public long getPendingDeliveries() {
        return pendingDeliveries;
    }

    public void setPendingDeliveries(long pendingDeliveries) {
        this.pendingDeliveries = pendingDeliveries;
    }

    public long getScheduledTransfers() {
        return scheduledTransfers;
    }

    public void setScheduledTransfers(long scheduledTransfers) {
        this.scheduledTransfers = scheduledTransfers;
    }

    // Builder

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int totalProducts;
        private int lowStockCount;
        private int outOfStockCount;
        private long pendingReceipts;
        private long pendingDeliveries;
        private long scheduledTransfers;

        public Builder totalProducts(int totalProducts) {
            this.totalProducts = totalProducts;
            return this;
        }

        public Builder lowStockCount(int lowStockCount) {
            this.lowStockCount = lowStockCount;
            return this;
        }

        public Builder outOfStockCount(int outOfStockCount) {
            this.outOfStockCount = outOfStockCount;
            return this;
        }

        public Builder pendingReceipts(long pendingReceipts) {
            this.pendingReceipts = pendingReceipts;
            return this;
        }

        public Builder pendingDeliveries(long pendingDeliveries) {
            this.pendingDeliveries = pendingDeliveries;
            return this;
        }

        public Builder scheduledTransfers(long scheduledTransfers) {
            this.scheduledTransfers = scheduledTransfers;
            return this;
        }

        public DashboardKpiResponse build() {
            return new DashboardKpiResponse(
                    totalProducts,
                    lowStockCount,
                    outOfStockCount,
                    pendingReceipts,
                    pendingDeliveries,
                    scheduledTransfers
            );
        }
    }
}