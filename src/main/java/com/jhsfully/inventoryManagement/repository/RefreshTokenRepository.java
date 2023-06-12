package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
