package org.project4.test_intern.repository;

import org.project4.test_intern.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository  extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByUserName(String username);
    @Query("select  u from UserEntity u where u.email = :email  ")
    List<UserEntity> findByEmail(@Param("email") String email);
    List<UserEntity> findByUserNameAndEmail(String username, String email);
    Boolean existsByEmail(String Email);

 }
