package org.project4.test_intern.repository;

import org.project4.test_intern.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository  extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByUserName(String username);
    List<UserEntity> findByEmail(String email);
    List<UserEntity> findByUserNameAndEmail(String username, String email);
 }
