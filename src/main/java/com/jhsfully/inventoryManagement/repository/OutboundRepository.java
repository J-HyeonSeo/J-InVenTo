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

    List<OutboundEntity> findByAtBetween(LocalDateTime start, LocalDateTime end);

}
