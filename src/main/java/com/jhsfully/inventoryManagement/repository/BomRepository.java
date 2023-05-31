package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.model.BOMEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BomRepository extends JpaRepository<BOMEntity, Long> {

    //select
    List<BOMEntity> findByPid(Long pid);
    List<BOMEntity> findByCid(Long cid);

    //exists
    boolean existsByPid(Long pid);
    boolean existsByCid(Long cid);
    boolean existsByPidOrCid(Long pid, Long cid);

    //delete
    void deleteByPid(Long pid);
}
