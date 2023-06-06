package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByEnabledIsTrue();
    List<ProductEntity> findByEnabledIsFalse();

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM outbound o , stocks s, plan pl, purchase pu " +
            "WHERE o.product = :product " +
            "AND s.product = :product " +
            "AND pl.product = :product " +
            "AND pu.product = :product"
    )
    boolean isProductReferenced(ProductEntity product);

}
