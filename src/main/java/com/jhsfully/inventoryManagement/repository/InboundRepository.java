package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.dto.InboundDto;
import com.jhsfully.inventoryManagement.model.InboundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InboundRepository extends JpaRepository<InboundEntity, Long> {

    @Query(
        "SELECT " +
        "new com.jhsfully.inventoryManagement.dto.InboundDto$InboundResponse" +
        "(i.id, i.purchaseid, prod.name, i.at, purchase.at, i.amount, i.company, i.note) " +
        "FROM inbound i " +
        "JOIN stocks s ON i.stockid = s.id " +
        "JOIN purchase purchase ON purchase.id = i.purchaseid " +
        "JOIN productinfo prod ON prod.id = s.productid " +
        "WHERE i.at BETWEEN ?1 AND ?2"
    )
    List<InboundDto.InboundResponse> findInbounds(LocalDateTime start, LocalDateTime end);

    boolean existsByPurchaseid(Long purchaseid);
}
