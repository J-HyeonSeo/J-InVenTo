package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.entity.ProductEntity;
import com.jhsfully.inventoryManagement.entity.StocksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StocksRepository extends JpaRepository<StocksEntity, Long> {

    @Query(
            "SELECT new com.jhsfully.inventoryManagement.dto.StocksDto$StockGroupResponse" +
                    "(s.product.id, SUM(s.amount), SUM(s.amount * s.inbound.purchase.price)) " +
                    "FROM stocks s " +
                    "GROUP BY s.product"
    )
    List<StocksDto.StockGroupResponse> getStocksGroupProduct();

    @Query(
            "SELECT s FROM stocks s WHERE s.product = ?1 and s.amount > 0"
    )
    List<StocksEntity> findByProduct(ProductEntity product);

}
