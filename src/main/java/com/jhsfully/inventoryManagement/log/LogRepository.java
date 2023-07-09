package com.jhsfully.inventoryManagement.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {

    List<LogEntity> findByAtBetween(LocalDateTime start, LocalDateTime end);

    List<LogEntity> findByAtBefore(LocalDateTime dateTime);

}
