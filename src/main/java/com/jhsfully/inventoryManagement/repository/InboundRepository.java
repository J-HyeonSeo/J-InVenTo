package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.dto.InboundDto;
import com.jhsfully.inventoryManagement.model.InboundEntity;
import com.jhsfully.inventoryManagement.model.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InboundRepository extends JpaRepository<InboundEntity, Long> {

    List<InboundEntity> findByAtBetween(LocalDateTime start, LocalDateTime end);

    List<InboundEntity> findByPurchase(PurchaseEntity purchase);

    boolean existsByPurchase(PurchaseEntity purchase);
}
