package com.example.bee_shirt.repository;

import com.example.bee_shirt.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByCode(String code);

    Optional<Account> findByUsername(String username);

    Optional<Account> findByCode(String code);
}
