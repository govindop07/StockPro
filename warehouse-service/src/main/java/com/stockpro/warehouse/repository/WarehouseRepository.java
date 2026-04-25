package com.stockpro.warehouse.repository;

import com.stockpro.warehouse.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Optional<Warehouse> findByWarehouseId(Long warehouseId);

    List<Warehouse> findByManagerId(Long managerId);

    List<Warehouse> findByIsActive(Boolean isActive);

    List<Warehouse> findByLocation(String location);

    Long countByIsActive(Boolean isActive);
}
