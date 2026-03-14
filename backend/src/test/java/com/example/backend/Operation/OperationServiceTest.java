package com.example.backend.Operation;

import com.example.backend.ledger.service.StockMovementService;
import com.example.backend.operation.dto.OperationMapper;
import com.example.backend.operation.dto.OperationResponse;
import com.example.backend.operation.entity.Operation;
import com.example.backend.operation.entity.OperationLine;
import com.example.backend.operation.entity.OperationStatus;
import com.example.backend.operation.entity.OperationType;
import com.example.backend.operation.repository.OperationRepository;
import com.example.backend.operation.service.OperationService;
import com.example.backend.product.entity.Product;
import com.example.backend.product.entity.ProductStock;
import com.example.backend.product.repository.ProductRepository;
import com.example.backend.product.repository.ProductStockRepository;
import com.example.backend.user.repository.UserRepository;
import com.example.backend.warehouse.entity.Warehouse;
import com.example.backend.warehouse.entity.WarehouseLocation;
import com.example.backend.warehouse.repository.WarehouseLocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperationServiceTest {

    @Mock
    OperationRepository operationRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    ProductStockRepository productStockRepository;
    @Mock
    WarehouseLocationRepository locationRepository;
    @Mock
    StockMovementService movementService;
    @Mock
    OperationMapper mapper;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    OperationService operationService;

    private Product product;
    private WarehouseLocation location;
    private Operation receiptOp;

    @BeforeEach
    void setUp() {
        Warehouse warehouse = Warehouse.builder().id(1L).name("Main Warehouse").build();
        location = WarehouseLocation.builder().id(1L).name("Rack A").warehouse(warehouse).build();

        product = Product.builder()
                .id(1L).name("Steel Rod").sku("STL-001")
                .unitOfMeasure("kg").reorderLevel(10).build();

        OperationLine line = OperationLine.builder()
                .id(1L).product(product).expectedQty(50).doneQty(0).build();

        receiptOp = Operation.builder()
                .id(1L).reference("REC-0001")
                .type(OperationType.RECEIPT)
                .status(OperationStatus.READY)
                .toLocation(location)
                .lines(new ArrayList<>(List.of(line)))
                .build();
        line.setOperation(receiptOp);

        // Mock security context so getCurrentUser() doesn't NPE
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn("anonymous");
        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);
    }

    // ── Receipt validate: stock +qty ──────────────────────────────────────────

    @Test
    void validateReceipt_addsStock() {
        when(operationRepository.findById(1L)).thenReturn(Optional.of(receiptOp));
        when(productStockRepository.findByProductIdAndLocationId(1L, 1L))
                .thenReturn(Optional.empty());
        when(productStockRepository.save(any(ProductStock.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        when(operationRepository.save(any(Operation.class))).thenReturn(receiptOp);
        when(mapper.toResponse(any())).thenReturn(new OperationResponse());

        operationService.validateReceipt(1L);

        verify(productStockRepository, atLeastOnce()).save(argThat(
                stock -> stock.getQuantity() == 50
        ));
        assertThat(receiptOp.getStatus()).isEqualTo(OperationStatus.DONE);
        verify(movementService).record(any(), any(), eq(OperationType.RECEIPT), isNull(), any(), eq(50));
    }

    // ── Delivery validate: stock −qty ─────────────────────────────────────────

    @Test
    void validateDelivery_subtractsStock() {
        OperationLine deliveryLine = OperationLine.builder()
                .id(2L).product(product).expectedQty(20).doneQty(0).build();
        Operation deliveryOp = Operation.builder()
                .id(2L).reference("DEL-0001")
                .type(OperationType.DELIVERY)
                .status(OperationStatus.READY)
                .fromLocation(location)
                .lines(new ArrayList<>(List.of(deliveryLine)))
                .build();
        deliveryLine.setOperation(deliveryOp);

        ProductStock existingStock = ProductStock.builder()
                .product(product).location(location).quantity(100).build();

        when(operationRepository.findById(2L)).thenReturn(Optional.of(deliveryOp));
        when(productStockRepository.findByProductIdAndLocationId(1L, 1L))
                .thenReturn(Optional.of(existingStock));
        when(productStockRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(operationRepository.save(any())).thenReturn(deliveryOp);
        when(mapper.toResponse(any())).thenReturn(new OperationResponse());

        operationService.validateDelivery(2L);

        assertThat(existingStock.getQuantity()).isEqualTo(80);
        verify(movementService).record(any(), any(), eq(OperationType.DELIVERY), any(), isNull(), eq(20));
    }

    // ── Delivery validate: insufficient stock → exception ────────────────────

    @Test
    void validateDelivery_insufficientStock_throwsException() {
        OperationLine line = OperationLine.builder()
                .id(3L).product(product).expectedQty(200).doneQty(0).build();
        Operation op = Operation.builder()
                .id(3L).reference("DEL-0002")
                .type(OperationType.DELIVERY)
                .status(OperationStatus.READY)
                .fromLocation(location)
                .lines(new ArrayList<>(List.of(line)))
                .build();
        line.setOperation(op);

        ProductStock thinStock = ProductStock.builder()
                .product(product).location(location).quantity(10).build();

        when(operationRepository.findById(3L)).thenReturn(Optional.of(op));
        when(productStockRepository.findByProductIdAndLocationId(1L, 1L))
                .thenReturn(Optional.of(thinStock));

        assertThatThrownBy(() -> operationService.validateDelivery(3L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Insufficient stock");
    }

    // ── Transfer validate: moves stock between locations ─────────────────────

    @Test
    void validateTransfer_movesStock() {
        Warehouse wh2 = Warehouse.builder().id(2L).name("Warehouse 2").build();
        WarehouseLocation dest = WarehouseLocation.builder().id(2L).name("Rack B").warehouse(wh2).build();

        OperationLine line = OperationLine.builder()
                .id(4L).product(product).expectedQty(30).doneQty(0).build();
        Operation transferOp = Operation.builder()
                .id(4L).reference("TRF-0001")
                .type(OperationType.TRANSFER)
                .status(OperationStatus.READY)
                .fromLocation(location)
                .toLocation(dest)
                .lines(new ArrayList<>(List.of(line)))
                .build();
        line.setOperation(transferOp);

        ProductStock srcStock = ProductStock.builder()
                .product(product).location(location).quantity(80).build();

        when(operationRepository.findById(4L)).thenReturn(Optional.of(transferOp));
        when(productStockRepository.findByProductIdAndLocationId(1L, 1L))
                .thenReturn(Optional.of(srcStock));
        when(productStockRepository.findByProductIdAndLocationId(1L, 2L))
                .thenReturn(Optional.empty());
        when(productStockRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(operationRepository.save(any())).thenReturn(transferOp);
        when(mapper.toResponse(any())).thenReturn(new OperationResponse());

        operationService.validateTransfer(4L);

        assertThat(srcStock.getQuantity()).isEqualTo(50);
        verify(movementService).record(any(), any(), eq(OperationType.TRANSFER), eq(location), eq(dest), eq(30));
    }

    // ── Cancel: DONE operation cannot be canceled ─────────────────────────────

    @Test
    void cancel_doneOperation_throwsException() {
        receiptOp.setStatus(OperationStatus.DONE);
        when(operationRepository.findById(1L)).thenReturn(Optional.of(receiptOp));

        assertThatThrownBy(() -> operationService.cancelOperation(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot cancel");
    }
}

