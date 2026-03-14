package com.example.backend.product.service;

import com.example.backend.product.dto.ProductMapper;
import com.example.backend.product.dto.ProductRequest;
import com.example.backend.product.dto.ProductResponse;
import com.example.backend.product.dto.ProductStockSummary;
import com.example.backend.product.entity.Product;
import com.example.backend.product.entity.ProductStock;
import com.example.backend.product.repository.ProductRepository;
import com.example.backend.product.repository.ProductStockRepository;
import com.example.backend.warehouse.entity.WarehouseLocation;
import com.example.backend.warehouse.repository.WarehouseLocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class ProductService {

    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;
    private final WarehouseLocationRepository locationRepository;
    private final ProductMapper mapper;

    public ProductService(ProductRepository productRepository, ProductStockRepository productStockRepository, WarehouseLocationRepository locationRepository, ProductMapper mapper) {
        this.productRepository = productRepository;
        this.productStockRepository = productStockRepository;
        this.locationRepository = locationRepository;
        this.mapper = mapper;
    }

    // ── List all products ──────────────────────────────────────────────────────

    public List<ProductResponse> getAllProducts(String search, String category) {
        List<Product> products;

        if (search != null && !search.isBlank()) {
            products = productRepository
                    .findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(search, search);
        } else if (category != null && !category.isBlank()) {
            products = productRepository.findByCategory(category);
        } else {
            products = productRepository.findAll();
        }

        return products.stream()
                .map(p -> {
                    Integer total = productStockRepository.sumQuantityByProductId(p.getId());
                    return mapper.toResponse(p, total == null ? 0 : total);
                })
                .collect(Collectors.toList());
    }

    // ── Get single product ─────────────────────────────────────────────────────

    public ProductResponse getProductById(Long id) {
        Product product = findProductOrThrow(id);
        Integer total = productStockRepository.sumQuantityByProductId(id);
        return mapper.toResponse(product, total == null ? 0 : total);
    }

    // ── Create product ─────────────────────────────────────────────────────────

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException("SKU '" + request.getSku() + "' already exists.");
        }

        Product product = Product.builder()
                .name(request.getName())
                .sku(request.getSku().toUpperCase())
                .category(request.getCategory())
                .unitOfMeasure(request.getUnitOfMeasure())
                .reorderLevel(request.getReorderLevel())
                .description(request.getDescription())
                .build();

        product = productRepository.save(product);

        // Seed initial stock if provided
        int initialStock = 0;
        if (request.getInitialStock() > 0 && request.getInitialLocationId() != null) {
            WarehouseLocation location = locationRepository
                    .findById(request.getInitialLocationId())
                    .orElseThrow(() -> new IllegalArgumentException("Location not found."));

            ProductStock stock = ProductStock.builder()
                    .product(product)
                    .location(location)
                    .quantity(request.getInitialStock())
                    .build();
            productStockRepository.save(stock);
            initialStock = request.getInitialStock();
        }

        return mapper.toResponse(product, initialStock);
    }

    // ── Update product ─────────────────────────────────────────────────────────

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = findProductOrThrow(id);

        // Allow SKU change only if it's not taken by another product
        if (!product.getSku().equalsIgnoreCase(request.getSku()) &&
                productRepository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException("SKU '" + request.getSku() + "' is already used by another product.");
        }

        product.setName(request.getName());
        product.setSku(request.getSku().toUpperCase());
        product.setCategory(request.getCategory());
        product.setUnitOfMeasure(request.getUnitOfMeasure());
        product.setReorderLevel(request.getReorderLevel());
        product.setDescription(request.getDescription());

        productRepository.save(product);

        Integer total = productStockRepository.sumQuantityByProductId(id);
        return mapper.toResponse(product, total == null ? 0 : total);
    }

    // ── Delete product ─────────────────────────────────────────────────────────

    @Transactional
    public void deleteProduct(Long id) {
        Product product = findProductOrThrow(id);
        Integer total = productStockRepository.sumQuantityByProductId(id);
        if (total != null && total > 0) {
            throw new IllegalStateException(
                    "Cannot delete product with stock on hand (" + total + " units). Clear stock first."
            );
        }
        productRepository.delete(product);
    }

    // ── Stock per location ─────────────────────────────────────────────────────

    public ProductStockSummary getStockByProduct(Long productId) {
        Product product = findProductOrThrow(productId);
        List<ProductStock> stocks = productStockRepository.findByProductId(productId);
        return mapper.toStockSummary(product, stocks);
    }

    // ── Low stock alert list ───────────────────────────────────────────────────

    public List<ProductResponse> getLowStockProducts() {
        return productRepository.findAll().stream()
                .filter(p -> {
                    Integer total = productStockRepository.sumQuantityByProductId(p.getId());
                    return total == null || total <= p.getReorderLevel();
                })
                .map(p -> {
                    Integer total = productStockRepository.sumQuantityByProductId(p.getId());
                    return mapper.toResponse(p, total == null ? 0 : total);
                })
                .collect(Collectors.toList());
    }

    // ── Categories ─────────────────────────────────────────────────────────────

    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
    }
}

