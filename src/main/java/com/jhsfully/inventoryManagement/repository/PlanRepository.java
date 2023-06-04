package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.model.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<PlanEntity, Long> {

    List<PlanEntity> findByDueGreaterThanEqual(LocalDate due);
    List<PlanEntity> findByDueBetween(LocalDate startDate, LocalDate endDate);

}
