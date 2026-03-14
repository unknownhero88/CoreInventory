package com.example.backend.product.dto;


import com.example.backend.product.entity.Product;
import com.example.backend.product.entity.ProductStock;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product, int totalStock) {
        ProductResponse res = new ProductResponse();
        res.setId(product.getId());
        res.setName(product.getName());
        res.setSku(product.getSku());
        res.setCategory(product.getCategory());
        res.setUnitOfMeasure(product.getUnitOfMeasure());
        res.setReorderLevel(product.getReorderLevel());
        res.setDescription(product.getDescription());
        res.setTotalStock(totalStock);
        res.setLowStock(totalStock <= product.getReorderLevel());
        res.setCreatedAt(product.getCreatedAt());
        res.setUpdatedAt(product.getUpdatedAt());
        return res;
    }

    public StockByLocationResponse toStockByLocation(ProductStock stock) {
        StockByLocationResponse res = new StockByLocationResponse();
        res.setLocationId(stock.getLocation().getId());
        res.setLocationName(stock.getLocation().getName());
        res.setWarehouseName(stock.getLocation().getWarehouse().getName());
        res.setQuantity(stock.getQuantity());
        res.setLowStock(stock.getQuantity() <= stock.getProduct().getReorderLevel());
        return res;
    }

    public ProductStockSummary toStockSummary(Product product, List<ProductStock> stocks) {
        int total = stocks.stream().mapToInt(ProductStock::getQuantity).sum();

        ProductStockSummary summary = new ProductStockSummary();
        summary.setProductId(product.getId());
        summary.setProductName(product.getName());
        summary.setSku(product.getSku());
        summary.setUnitOfMeasure(product.getUnitOfMeasure());
        summary.setTotalQuantity(total);
        summary.setReorderLevel(product.getReorderLevel());
        summary.setLowStock(total <= product.getReorderLevel());
        summary.setStockByLocation(
                stocks.stream().map(this::toStockByLocation).collect(Collectors.toList())
        );
        return summary;
    }
}

