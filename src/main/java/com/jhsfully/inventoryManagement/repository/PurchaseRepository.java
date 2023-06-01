package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.model.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {

    List<PurchaseEntity> findByAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

}
