package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.entity.InboundEntity;
import com.jhsfully.inventoryManagement.entity.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InboundRepository extends JpaRepository<InboundEntity, Long> {

    List<InboundEntity> findByAtBetween(LocalDateTime start, LocalDateTime end);

    @Query(
            "SELECT SUM(i.amount) " +
            "FROM inbound i " +
            "WHERE i.purchase = ?1"
    )
    Double findByPurchase(PurchaseEntity purchase);

    boolean existsByPurchase(PurchaseEntity purchase);
}
