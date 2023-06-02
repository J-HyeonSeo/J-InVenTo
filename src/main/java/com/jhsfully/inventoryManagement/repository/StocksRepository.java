package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.model.StocksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StocksRepository extends JpaRepository<StocksEntity, Long> {

    @Query(
            "SELECT new com.jhsfully.inventoryManagement.dto.StocksDto$StockResponse(s.pid, p.name, p.spec, SUM(s.amount)) " +
                    "FROM stocks s " +
                    "JOIN productinfo p on s.pid = p.id " +
                    "GROUP BY s.pid"
    )
    List<StocksDto.StockResponse> findStockGroupPid();

    List<StocksEntity> findByPid(Long pid);

}
