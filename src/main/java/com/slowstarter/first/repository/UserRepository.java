package com.slowstarter.first.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slowstarter.first.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>
{
    boolean existsByUsername(String username);

    UserEntity findByUsername(String username);
}
