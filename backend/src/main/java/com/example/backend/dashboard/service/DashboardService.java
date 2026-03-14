package com.example.backend.dashboard.service;

import com.example.backend.dashboard.dto.DashboardKpiResponse;
import com.example.backend.operation.entity.OperationStatus;
import com.example.backend.operation.entity.OperationType;
import com.example.backend.operation.repository.OperationRepository;
import com.example.backend.product.repository.ProductRepository;
import com.example.backend.product.repository.ProductStockRepository;
import org.springframework.stereotype.Service;

@Service

public class DashboardService {

    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;
    private final OperationRepository operationRepository;

    public DashboardService(ProductRepository productRepository, ProductStockRepository productStockRepository, OperationRepository operationRepository) {
        this.productRepository = productRepository;
        this.productStockRepository = productStockRepository;
        this.operationRepository = operationRepository;
    }

    public DashboardKpiResponse getKpis() {

        int totalProducts = (int) productRepository.count();

        // Low stock: total qty <= reorder level (but > 0)
        int lowStockCount = (int) productRepository.findAll().stream()
                .filter(p -> {
                    Integer qty = productStockRepository.sumQuantityByProductId(p.getId());
                    int total   = qty == null ? 0 : qty;
                    return total > 0 && total <= p.getReorderLevel();
                }).count();

        // Out of stock: qty == 0
        int outOfStockCount = (int) productRepository.findAll().stream()
                .filter(p -> {
                    Integer qty = productStockRepository.sumQuantityByProductId(p.getId());
                    return qty == null || qty == 0;
                }).count();

        long pendingReceipts = operationRepository.countByTypeAndStatus(
                OperationType.RECEIPT, OperationStatus.READY);

        long pendingDeliveries = operationRepository.countByTypeAndStatus(
                OperationType.DELIVERY, OperationStatus.READY);

        long scheduledTransfers = operationRepository.countByTypeAndStatus(
                OperationType.TRANSFER, OperationStatus.READY);

        return DashboardKpiResponse.builder()
                .totalProducts(totalProducts)
                .lowStockCount(lowStockCount)
                .outOfStockCount(outOfStockCount)
                .pendingReceipts(pendingReceipts)
                .pendingDeliveries(pendingDeliveries)
                .scheduledTransfers(scheduledTransfers)
                .build();
    }
}

