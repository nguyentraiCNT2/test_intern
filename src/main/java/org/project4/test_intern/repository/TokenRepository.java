package org.project4.test_intern.repository;

import org.project4.test_intern.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    List<TokenEntity> findByToken(String token);
    Boolean existsByToken(String token);
}
