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
            "INNER JOIN outbound o ON p = o.product " +
            "INNER JOIN stocks s ON o.product = s.product " +
            "INNER JOIN plan pl ON s.product = pl.product " +
            "INNER JOIN purchase pu ON pl.product = pu.product " +
            "WHERE p = ?1")
    boolean isProductReferenced(ProductEntity product);

}
