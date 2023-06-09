package com.jhsfully.inventoryManagement.repository;

import com.jhsfully.inventoryManagement.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
