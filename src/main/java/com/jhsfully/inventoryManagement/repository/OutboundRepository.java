package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.dto.OutboundDto;
import com.jhsfully.inventoryManagement.model.OutboundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OutboundRepository extends JpaRepository<OutboundEntity, Long> {

    @Query(
            "SELECT " +
            "new com.jhsfully.inventoryManagement.dto.OutboundDto$OutboundResponse" +
            "(o.id, p.name, o.destination, o.amount, o.at, o.note) " +
            "FROM outbound o " +
            "JOIN productinfo p ON p.id = o.productid " +
            "WHERE o.at BETWEEN ?1 AND ?2"
    )
    List<OutboundDto.OutboundResponse> getOutbounds(LocalDateTime start, LocalDateTime end);

}
