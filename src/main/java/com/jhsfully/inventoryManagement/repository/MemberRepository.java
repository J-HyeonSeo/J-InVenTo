package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {



}
