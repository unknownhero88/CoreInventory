package com.example.backend.Product;
import com.example.backend.product.dto.ProductMapper;
import com.example.backend.product.dto.ProductRequest;
import com.example.backend.product.dto.ProductResponse;
import com.example.backend.product.entity.Product;
import com.example.backend.product.repository.ProductRepository;
import com.example.backend.product.repository.ProductStockRepository;
import com.example.backend.product.service.ProductService;
import com.example.backend.warehouse.repository.WarehouseLocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;
    @Mock
    ProductStockRepository productStockRepository;
    @Mock
    WarehouseLocationRepository locationRepository;
    @Mock
    ProductMapper mapper;

    @InjectMocks
    ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder()
                .id(1L).name("Steel Rod").sku("STL-001")
                .category("Metal").unitOfMeasure("kg").reorderLevel(10)
                .build();
    }

    @Test
    void createProduct_success() {
        ProductRequest req = new ProductRequest();
        req.setName("Steel Rod");
        req.setSku("STL-001");
        req.setCategory("Metal");
        req.setUnitOfMeasure("kg");
        req.setReorderLevel(10);

        when(productRepository.existsBySku("STL-001")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductResponse mockRes = new ProductResponse();
        mockRes.setId(1L);
        mockRes.setName("Steel Rod");
        when(mapper.toResponse(any(Product.class), anyInt())).thenReturn(mockRes);

        ProductResponse res = productService.createProduct(req);

        assertThat(res.getId()).isEqualTo(1L);
        assertThat(res.getName()).isEqualTo("Steel Rod");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_duplicateSku_throwsException() {
        ProductRequest req = new ProductRequest();
        req.setSku("STL-001");
        req.setName("Duplicate");
        req.setUnitOfMeasure("kg");

        when(productRepository.existsBySku("STL-001")).thenReturn(true);

        assertThatThrownBy(() -> productService.createProduct(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("STL-001");
    }

    @Test
    void deleteProduct_withStock_throwsException() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productStockRepository.sumQuantityByProductId(1L)).thenReturn(50);

        assertThatThrownBy(() -> productService.deleteProduct(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("stock on hand");
    }

    @Test
    void deleteProduct_noStock_succeeds() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productStockRepository.sumQuantityByProductId(1L)).thenReturn(0);

        assertThatNoException().isThrownBy(() -> productService.deleteProduct(1L));
        verify(productRepository).delete(sampleProduct);
    }

    @Test
    void getAllProducts_withSearch_delegatesToRepo() {
        when(productRepository.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase("steel", "steel"))
                .thenReturn(List.of(sampleProduct));
        when(productStockRepository.sumQuantityByProductId(1L)).thenReturn(100);

        ProductResponse mockRes = new ProductResponse();
        when(mapper.toResponse(any(), anyInt())).thenReturn(mockRes);

        List<ProductResponse> results = productService.getAllProducts("steel", null);

        assertThat(results).hasSize(1);
    }
}
