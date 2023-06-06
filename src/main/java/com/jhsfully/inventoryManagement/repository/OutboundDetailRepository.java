package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.dto.OutboundDto;
import com.jhsfully.inventoryManagement.model.OutboundDetailsEntity;
import com.jhsfully.inventoryManagement.model.OutboundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboundDetailRepository extends JpaRepository<OutboundDetailsEntity, Long> {
    List<OutboundDto.OutboundDetailResponse> findByOutbound(OutboundEntity outbound);
}
