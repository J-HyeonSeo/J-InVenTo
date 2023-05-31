package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.model.BOMEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BomRepository extends JpaRepository<BOMEntity, Long> {

    List<BOMEntity> findByPid(Long pid);
    List<BOMEntity> findByCid(Long cid);
    boolean existsByCid(Long cid);

}
