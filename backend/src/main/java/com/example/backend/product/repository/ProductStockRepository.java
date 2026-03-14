package com.example.backend.product.repository;

import com.example.backend.product.entity.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {

    List<ProductStock> findByProductId(Long productId);

    Optional<ProductStock> findByProductIdAndLocationId(Long productId, Long locationId);

    @Query("SELECT SUM(ps.quantity) FROM ProductStock ps WHERE ps.product.id = :productId")
    Integer sumQuantityByProductId(@Param("productId") Long productId);

    @Query("""
SELECT ps FROM ProductStock ps
JOIN FETCH ps.product p
JOIN FETCH ps.location l
WHERE ps.quantity <= p.reorderLevel
""")
    List<ProductStock> findLowStockItems();

    @Query("""
        SELECT ps FROM ProductStock ps
        JOIN FETCH ps.product p
        JOIN FETCH ps.location l
        WHERE l.warehouse.id = :warehouseId
        """)
    List<ProductStock> findByWarehouseId(@Param("warehouseId") Long warehouseId);
}
