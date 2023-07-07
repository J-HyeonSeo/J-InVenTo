package com.jhsfully.inventoryManagement.aop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {

    List<LogEntity> findByAtBetween(LocalDateTime start, LocalDateTime end);

    void deleteByAtBetween(LocalDateTime start, LocalDateTime end);

}
