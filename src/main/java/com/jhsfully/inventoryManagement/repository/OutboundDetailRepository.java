package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.dto.OutboundDto;
import com.jhsfully.inventoryManagement.model.OutboundDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboundDetailRepository extends JpaRepository<OutboundDetailsEntity, Long> {
    @Query(
            "SELECT " +
            "new com.jhsfully.inventoryManagement.dto.OutboundDto$OutboundDetailResponse" +
            "(od.id, s.id, p.name, od.amount) " +
            "FROM outbounddetails od " +
            "JOIN stocks s ON s.id = od.stockid " +
            "JOIN productinfo p ON p.id = s.productid " +
            "WHERE od.id = ?1"
    )
    List<OutboundDto.OutboundDetailResponse> getOutboundDetails(Long outboundId);
}
