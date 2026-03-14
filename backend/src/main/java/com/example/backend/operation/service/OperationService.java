package com.example.backend.operation.service;

import com.example.backend.ledger.service.StockMovementService;
import com.example.backend.operation.dto.*;
import com.example.backend.operation.entity.Operation;
import com.example.backend.operation.entity.OperationLine;
import com.example.backend.operation.entity.OperationStatus;
import com.example.backend.operation.entity.OperationType;
import com.example.backend.operation.repository.OperationRepository;
import com.example.backend.product.entity.Product;
import com.example.backend.product.repository.ProductRepository;
import com.example.backend.product.repository.ProductStockRepository;
import com.example.backend.user.entity.User;
import com.example.backend.product.entity.ProductStock;
import com.example.backend.user.repository.UserRepository;
import com.example.backend.warehouse.entity.WarehouseLocation;
import com.example.backend.warehouse.repository.WarehouseLocationRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.time.LocalDateTime;
import java.util.List;

import java.util.stream.Collectors;

@Service

public class OperationService {

    private final OperationRepository operationRepository;
    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;
    private final WarehouseLocationRepository locationRepository;
    private final StockMovementService movementService;
    private final OperationMapper mapper;
    private final UserRepository userRepository;

    public OperationService(OperationRepository operationRepository, ProductRepository productRepository, ProductStockRepository productStockRepository, WarehouseLocationRepository locationRepository, StockMovementService movementService, OperationMapper mapper, UserRepository userRepository) {
        this.operationRepository = operationRepository;
        this.productRepository = productRepository;
        this.productStockRepository = productStockRepository;
        this.locationRepository = locationRepository;
        this.movementService = movementService;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    // ── Reference generator ───────────────────────────────────────────────────

    private String generateReference(OperationType type) {
        String prefix = switch (type) {
            case RECEIPT    -> "REC";
            case DELIVERY   -> "DEL";
            case TRANSFER   -> "TRF";
            case ADJUSTMENT -> "ADJ";
        };
        int next = operationRepository.findMaxSequenceByType(type) + 1;
        return String.format("%s-%04d", prefix, next);
    }

    // ── List ──────────────────────────────────────────────────────────────────

    public List<OperationResponse> getAll(OperationType type, OperationStatus status) {
        List<Operation> ops;
        if (type != null && status != null) {
            ops = operationRepository.findByTypeAndStatusOrderByCreatedAtDesc(type, status);
        } else if (type != null) {
            ops = operationRepository.findByTypeOrderByCreatedAtDesc(type);
        } else if (status != null) {
            ops = operationRepository.findByStatusOrderByCreatedAtDesc(status);
        } else {
            ops = operationRepository.findAllByOrderByCreatedAtDesc();
        }
        return ops.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    public OperationResponse getById(Long id) {
        return mapper.toResponse(findOrThrow(id));
    }

    // ── CREATE RECEIPT ────────────────────────────────────────────────────────

    @Transactional
    public OperationResponse createReceipt(ReceiptRequest req) {
        WarehouseLocation toLocation = findLocationOrThrow(req.getToLocationId());

        Operation op = Operation.builder()
                .reference(generateReference(OperationType.RECEIPT))
                .type(OperationType.RECEIPT)
                .status(OperationStatus.READY)
                .supplierName(req.getSupplierName())
                .toLocation(toLocation)
                .notes(req.getNotes())
                .createdBy(getCurrentUser())
                .build();

        attachLines(op, req.getLines());
        return mapper.toResponse(operationRepository.save(op));
    }

    // ── VALIDATE RECEIPT: stock +qty ──────────────────────────────────────────

    @Transactional
    public OperationResponse validateReceipt(Long id) {
        Operation op = findOrThrow(id);
        assertType(op, OperationType.RECEIPT);
        assertStatus(op, OperationStatus.READY, OperationStatus.DRAFT);

        for (OperationLine line : op.getLines()) {
            line.setDoneQty(line.getExpectedQty());
            addStock(line.getProduct(), op.getToLocation(), line.getExpectedQty());
            movementService.record(op, line.getProduct(), OperationType.RECEIPT,
                    null, op.getToLocation(), line.getExpectedQty());
        }

        op.setStatus(OperationStatus.DONE);
        op.setValidatedAt(LocalDateTime.now());
        return mapper.toResponse(operationRepository.save(op));
    }

    // ── CREATE DELIVERY ───────────────────────────────────────────────────────

    @Transactional
    public OperationResponse createDelivery(DeliveryRequest req) {
        WarehouseLocation fromLocation = findLocationOrThrow(req.getFromLocationId());

        Operation op = Operation.builder()
                .reference(generateReference(OperationType.DELIVERY))
                .type(OperationType.DELIVERY)
                .status(OperationStatus.READY)
                .customerRef(req.getCustomerRef())
                .fromLocation(fromLocation)
                .notes(req.getNotes())
                .createdBy(getCurrentUser())
                .build();

        attachLines(op, req.getLines());
        return mapper.toResponse(operationRepository.save(op));
    }

    // ── VALIDATE DELIVERY: stock −qty ─────────────────────────────────────────

    @Transactional
    public OperationResponse validateDelivery(Long id) {
        Operation op = findOrThrow(id);
        assertType(op, OperationType.DELIVERY);
        assertStatus(op, OperationStatus.READY, OperationStatus.DRAFT);

        for (OperationLine line : op.getLines()) {
            line.setDoneQty(line.getExpectedQty());
            subtractStock(line.getProduct(), op.getFromLocation(), line.getExpectedQty());
            movementService.record(op, line.getProduct(), OperationType.DELIVERY,
                    op.getFromLocation(), null, line.getExpectedQty());
        }

        op.setStatus(OperationStatus.DONE);
        op.setValidatedAt(LocalDateTime.now());
        return mapper.toResponse(operationRepository.save(op));
    }

    // ── CREATE TRANSFER ───────────────────────────────────────────────────────

    @Transactional
    public OperationResponse createTransfer(TransferRequest req) {
        if (req.getFromLocationId().equals(req.getToLocationId())) {
            throw new IllegalArgumentException("Source and destination locations cannot be the same.");
        }
        WarehouseLocation from = findLocationOrThrow(req.getFromLocationId());
        WarehouseLocation to   = findLocationOrThrow(req.getToLocationId());

        Operation op = Operation.builder()
                .reference(generateReference(OperationType.TRANSFER))
                .type(OperationType.TRANSFER)
                .status(OperationStatus.READY)
                .fromLocation(from)
                .toLocation(to)
                .notes(req.getNotes())
                .createdBy(getCurrentUser())
                .build();

        attachLines(op, req.getLines());
        return mapper.toResponse(operationRepository.save(op));
    }

    // ── VALIDATE TRANSFER: move stock between locations ───────────────────────

    @Transactional
    public OperationResponse validateTransfer(Long id) {
        Operation op = findOrThrow(id);
        assertType(op, OperationType.TRANSFER);
        assertStatus(op, OperationStatus.READY, OperationStatus.DRAFT);

        for (OperationLine line : op.getLines()) {
            line.setDoneQty(line.getExpectedQty());
            subtractStock(line.getProduct(), op.getFromLocation(), line.getExpectedQty());
            addStock(line.getProduct(), op.getToLocation(), line.getExpectedQty());
            movementService.record(op, line.getProduct(), OperationType.TRANSFER,
                    op.getFromLocation(), op.getToLocation(), line.getExpectedQty());
        }

        op.setStatus(OperationStatus.DONE);
        op.setValidatedAt(LocalDateTime.now());
        return mapper.toResponse(operationRepository.save(op));
    }

    // ── CREATE ADJUSTMENT ─────────────────────────────────────────────────────

    @Transactional
    public OperationResponse createAdjustment(AdjustmentRequest req) {
        WarehouseLocation location = findLocationOrThrow(req.getLocationId());

        Operation op = Operation.builder()
                .reference(generateReference(OperationType.ADJUSTMENT))
                .type(OperationType.ADJUSTMENT)
                .status(OperationStatus.READY)
                .toLocation(location)
                .notes(req.getNotes())
                .createdBy(getCurrentUser())
                .build();

        // Convert adjustment lines → operation lines (store counted qty as expectedQty)
        List<OperationLineRequest> lineRequests = req.getLines().stream()
                .map(l -> {
                    OperationLineRequest lr = new OperationLineRequest();
                    lr.setProductId(l.getProductId());
                    lr.setQuantity(l.getCountedQty());
                    return lr;
                }).collect(Collectors.toList());

        attachLines(op, lineRequests);
        return mapper.toResponse(operationRepository.save(op));
    }

    // ── VALIDATE ADJUSTMENT: set stock to counted qty ─────────────────────────

    @Transactional
    public OperationResponse validateAdjustment(Long id) {
        Operation op = findOrThrow(id);
        assertType(op, OperationType.ADJUSTMENT);
        assertStatus(op, OperationStatus.READY, OperationStatus.DRAFT);

        WarehouseLocation location = op.getToLocation();

        for (OperationLine line : op.getLines()) {
            int countedQty = line.getExpectedQty();
            Product product = line.getProduct();

            ProductStock stock = productStockRepository
                    .findByProductIdAndLocationId(product.getId(), location.getId())
                    .orElseGet(() -> ProductStock.builder()
                            .product(product).location(location).quantity(0).build());

            int diff = countedQty - stock.getQuantity();
            stock.setQuantity(countedQty);
            productStockRepository.save(stock);

            line.setDoneQty(countedQty);

            // Record ledger entry with actual diff quantity (can be negative)
            movementService.record(op, product, OperationType.ADJUSTMENT,
                    diff < 0 ? location : null,
                    diff >= 0 ? location : null,
                    Math.abs(diff));
        }

        op.setStatus(OperationStatus.DONE);
        op.setValidatedAt(LocalDateTime.now());
        return mapper.toResponse(operationRepository.save(op));
    }

    // ── CANCEL ────────────────────────────────────────────────────────────────

    @Transactional
    public OperationResponse cancelOperation(Long id) {
        Operation op = findOrThrow(id);
        if (op.getStatus() == OperationStatus.DONE) {
            throw new IllegalStateException("Cannot cancel a validated operation.");
        }
        op.setStatus(OperationStatus.CANCELED);
        return mapper.toResponse(operationRepository.save(op));
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private void attachLines(Operation op, List<OperationLineRequest> lineRequests) {
        for (OperationLineRequest lr : lineRequests) {
            Product product = productRepository.findById(lr.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + lr.getProductId()));

            OperationLine line = OperationLine.builder()
                    .operation(op)
                    .product(product)
                    .expectedQty(lr.getQuantity())
                    .doneQty(0)
                    .build();
            op.getLines().add(line);
        }
    }

    private void addStock(Product product, WarehouseLocation location, int qty) {
        ProductStock stock = productStockRepository
                .findByProductIdAndLocationId(product.getId(), location.getId())
                .orElseGet(() -> ProductStock.builder()
                        .product(product).location(location).quantity(0).build());
        stock.addQuantity(qty);
        productStockRepository.save(stock);
    }

    private void subtractStock(Product product, WarehouseLocation location, int qty) {
        ProductStock stock = productStockRepository
                .findByProductIdAndLocationId(product.getId(), location.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "No stock found for product '" + product.getName() + "' at location '" + location.getName() + "'"));
        stock.subtractQuantity(qty);
        productStockRepository.save(stock);
    }

    private WarehouseLocation findLocationOrThrow(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found: " + id));
    }

    private Operation findOrThrow(Long id) {
        return operationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Operation not found: " + id));
    }

    private void assertType(Operation op, OperationType expected) {
        if (op.getType() != expected) {
            throw new IllegalStateException("Operation " + op.getReference() + " is not a " + expected);
        }
    }

    private void assertStatus(Operation op, OperationStatus... allowed) {
        for (OperationStatus s : allowed) {
            if (op.getStatus() == s) return;
        }
        throw new IllegalStateException(
                "Operation " + op.getReference() + " cannot be validated in status: " + op.getStatus());
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails ud) {
            return userRepository.findByEmail(ud.getUsername()).orElse(null);
        }
        return null;
    }
}

