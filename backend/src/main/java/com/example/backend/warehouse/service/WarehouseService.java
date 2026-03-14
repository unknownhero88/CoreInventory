package com.example.backend.warehouse.service;

import com.example.backend.warehouse.dto.LocationRequest;
import com.example.backend.warehouse.dto.LocationResponse;
import com.example.backend.warehouse.dto.WarehouseRequest;
import com.example.backend.warehouse.dto.WarehouseResponse;
import com.example.backend.warehouse.entity.Warehouse;
import com.example.backend.warehouse.entity.WarehouseLocation;
import com.example.backend.warehouse.repository.WarehouseLocationRepository;
import com.example.backend.warehouse.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseLocationRepository locationRepository;

    public WarehouseService(WarehouseRepository warehouseRepository, WarehouseLocationRepository locationRepository) {
        this.warehouseRepository = warehouseRepository;
        this.locationRepository = locationRepository;
    }

    public List<WarehouseResponse> getAllWarehouses() {
        return warehouseRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public WarehouseResponse getWarehouse(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public WarehouseResponse createWarehouse(WarehouseRequest request) {
        if (warehouseRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Warehouse '" + request.getName() + "' already exists.");
        }
        Warehouse warehouse = Warehouse.builder()
                .name(request.getName())
                .address(request.getAddress())
                .build();
        return toResponse(warehouseRepository.save(warehouse));
    }

    @Transactional
    public WarehouseResponse updateWarehouse(Long id, WarehouseRequest request) {
        Warehouse warehouse = findOrThrow(id);
        warehouse.setName(request.getName());
        warehouse.setAddress(request.getAddress());
        return toResponse(warehouseRepository.save(warehouse));
    }

    @Transactional
    public LocationResponse addLocation(Long warehouseId, LocationRequest request) {
        Warehouse warehouse = findOrThrow(warehouseId);
        WarehouseLocation location = WarehouseLocation.builder()
                .warehouse(warehouse)
                .name(request.getName())
                .build();
        return toLocationResponse(locationRepository.save(location));
    }

    @Transactional
    public void deleteLocation(Long locationId) {
        locationRepository.deleteById(locationId);
    }

    public List<LocationResponse> getLocationsByWarehouse(Long warehouseId) {
        return locationRepository.findByWarehouseId(warehouseId).stream()
                .map(this::toLocationResponse)
                .collect(Collectors.toList());
    }

    // ── Mappers ───────────────────────────────────────────────────────────────

    private WarehouseResponse toResponse(Warehouse w) {
        WarehouseResponse res = new WarehouseResponse();
        res.setId(w.getId());
        res.setName(w.getName());
        res.setAddress(w.getAddress());
        res.setLocations(
                locationRepository.findByWarehouseId(w.getId()).stream()
                        .map(this::toLocationResponse)
                        .collect(Collectors.toList())
        );
        return res;
    }

    private LocationResponse toLocationResponse(WarehouseLocation loc) {
        LocationResponse res = new LocationResponse();
        res.setId(loc.getId());
        res.setName(loc.getName());
        res.setWarehouseId(loc.getWarehouse().getId());
        res.setWarehouseName(loc.getWarehouse().getName());
        return res;
    }

    private Warehouse findOrThrow(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found with id: " + id));
    }
}
