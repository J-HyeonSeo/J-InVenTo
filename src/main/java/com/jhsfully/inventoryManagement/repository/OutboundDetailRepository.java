package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.entity.OutboundDetailsEntity;
import com.jhsfully.inventoryManagement.entity.OutboundEntity;
import com.jhsfully.inventoryManagement.entity.StocksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboundDetailRepository extends JpaRepository<OutboundDetailsEntity, Long> {
    List<OutboundDetailsEntity> findByOutbound(OutboundEntity outbound);

    Double countByStock(StocksEntity stocksEntity);
}
