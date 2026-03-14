package com.example.backend.ledger.repository;

import com.example.backend.ledger.entity.StockMovement;
import com.example.backend.operation.entity.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findAllByOrderByMovedAtDesc();

    List<StockMovement> findByProductIdOrderByMovedAtDesc(Long productId);

    List<StockMovement> findByTypeOrderByMovedAtDesc(OperationType type);

    List<StockMovement> findByMovedAtBetweenOrderByMovedAtDesc(LocalDateTime from, LocalDateTime to);

    List<StockMovement> findByOperationId(Long operationId);
}
