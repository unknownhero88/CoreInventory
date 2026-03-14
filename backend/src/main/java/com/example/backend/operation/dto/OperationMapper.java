package com.example.backend.operation.dto;

import com.example.backend.operation.entity.Operation;
import com.example.backend.operation.entity.OperationLine;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OperationMapper {

    public OperationResponse toResponse(Operation op) {
        OperationResponse res = new OperationResponse();
        res.setId(op.getId());
        res.setReference(op.getReference());
        res.setType(op.getType());
        res.setStatus(op.getStatus());
        res.setSupplierName(op.getSupplierName());
        res.setCustomerRef(op.getCustomerRef());
        res.setNotes(op.getNotes());
        res.setCreatedAt(op.getCreatedAt());
        res.setValidatedAt(op.getValidatedAt());

        if (op.getCreatedBy() != null) {
            res.setCreatedBy(op.getCreatedBy().getName());
        }
        if (op.getFromLocation() != null) {
            res.setFromLocationName(
                    op.getFromLocation().getWarehouse().getName() + " / " + op.getFromLocation().getName()
            );
        }
        if (op.getToLocation() != null) {
            res.setToLocationName(
                    op.getToLocation().getWarehouse().getName() + " / " + op.getToLocation().getName()
            );
        }

        res.setLines(op.getLines().stream()
                .map(this::toLineResponse)
                .collect(Collectors.toList()));

        return res;
    }

    public OperationLineResponse toLineResponse(OperationLine line) {
        OperationLineResponse res = new OperationLineResponse();
        res.setId(line.getId());
        res.setProductId(line.getProduct().getId());
        res.setProductName(line.getProduct().getName());
        res.setSku(line.getProduct().getSku());
        res.setUnitOfMeasure(line.getProduct().getUnitOfMeasure());
        res.setExpectedQty(line.getExpectedQty());
        res.setDoneQty(line.getDoneQty());
        return res;
    }
}

