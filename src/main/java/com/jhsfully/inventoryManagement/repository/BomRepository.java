package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.dto.BomDto;
import com.jhsfully.inventoryManagement.model.BOMEntity;
import com.jhsfully.inventoryManagement.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BomRepository extends JpaRepository<BOMEntity, Long> {

    //select
    @Query(
            "SELECT " +
            "new com.jhsfully.inventoryManagement.dto.BomDto$BomTopResponse " +
            "(b.parentProduct.id, b.parentProduct.name) " +
            "FROM bom b " +
            "GROUP BY b.parentProduct"
    )
    List<BomDto.BomTopResponse> findByGroupParentProduct();
    List<BOMEntity> findByParentProduct(ProductEntity product);
    List<BOMEntity> findByChildProduct(ProductEntity product);

    //exists
    boolean existsByParentProduct(ProductEntity product);
    boolean existsByChildProduct(ProductEntity product);
    boolean existsByParentProductOrChildProduct(ProductEntity parent, ProductEntity child);

    //delete
    void deleteByParentProduct(ProductEntity product);
}
