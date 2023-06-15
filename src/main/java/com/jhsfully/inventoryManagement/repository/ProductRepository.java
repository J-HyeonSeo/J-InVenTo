package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByEnabledIsTrue();
    List<ProductEntity> findByEnabledIsFalse();

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM productinfo p " +
            "LEFT JOIN outbound o ON o.product = p " +
            "LEFT JOIN stocks s ON s.product = p " +
            "LEFT JOIN plan pl ON pl.product = p " +
            "LEFT JOIN purchase pu ON pu.product = p " +
            "WHERE p = ?1 " +
            "AND (p = o.product OR p = s.product OR p = pl.product OR p = pu.product)"
    )
    boolean isProductReferenced(ProductEntity product);

}
