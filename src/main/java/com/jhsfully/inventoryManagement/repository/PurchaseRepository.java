package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.dto.PurchaseDto;
import com.jhsfully.inventoryManagement.entity.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {

    @Query(
            "SELECT " +
            "new com.jhsfully.inventoryManagement.dto.PurchaseDto$PurchaseResponse" +
            "(p.id, p.product.id, p.product.name, p.at, p.amount, p.amount - COALESCE(SUM(i.amount), 0), p.price, p.company, p.note) " +
            "FROM purchase p " +
            "LEFT JOIN inbound i ON i.purchase = p " +
            "WHERE p.id = ?1 " +
            "GROUP BY p.id"
    )
    Optional<PurchaseDto.PurchaseResponse> getPurchase(Long id);

    @Query(
            "SELECT " +
            "new com.jhsfully.inventoryManagement.dto.PurchaseDto$PurchaseResponse" +
            "(p.id, p.product.id, p.product.name, p.at, p.amount, p.amount - COALESCE(SUM(i.amount), 0), p.price, p.company, p.note) " +
            "FROM purchase p " +
            "LEFT JOIN inbound i ON i.purchase = p " +
            "WHERE p.at BETWEEN ?1 AND ?2 " +
            "GROUP BY p.id"
    )
    List<PurchaseDto.PurchaseResponse> getPurchases(LocalDateTime startDateTime, LocalDateTime endDateTime);


//    List<PurchaseEntity> findByAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

}
