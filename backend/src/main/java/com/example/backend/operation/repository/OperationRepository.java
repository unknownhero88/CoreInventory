package com.example.backend.operation.repository;

import com.example.backend.operation.entity.Operation;
import com.example.backend.operation.entity.OperationStatus;
import com.example.backend.operation.entity.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    List<Operation> findByTypeOrderByCreatedAtDesc(OperationType type);

    List<Operation> findByStatusOrderByCreatedAtDesc(OperationStatus status);

    List<Operation> findByTypeAndStatusOrderByCreatedAtDesc(OperationType type, OperationStatus status);

    List<Operation> findAllByOrderByCreatedAtDesc();

    Optional<Operation> findByReference(String reference);

    long countByTypeAndStatus(OperationType type, OperationStatus status);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(o.reference, 5) AS int)), 0) FROM Operation o WHERE o.type = :type")
    int findMaxSequenceByType(@Param("type") OperationType type);
}