package com.example.backend.operation.repository;

import com.example.backend.operation.entity.OperationLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationLineRepository extends JpaRepository<OperationLine, Long> {
    List<OperationLine> findByOperationId(Long operationId);
}
