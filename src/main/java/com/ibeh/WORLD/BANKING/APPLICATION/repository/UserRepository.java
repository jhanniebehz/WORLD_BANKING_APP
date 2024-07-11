package com.ibeh.WORLD.BANKING.APPLICATION.repository;

import com.ibeh.WORLD.BANKING.APPLICATION.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByAccountNumber(String accountNumber);

    UserEntity findByAccountNumber(String accountNumber);
}
