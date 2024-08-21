package org.project4.test_intern.repository;

import org.project4.test_intern.entity.TokenEntity;
import org.project4.test_intern.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    List<TokenEntity> findByUserid(UserEntity user);
    List<TokenEntity> findByToken(String token);
}
