package com.example.bee_shirt.repository;

import com.example.bee_shirt.entity.ShirtDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShirtDetailRepository extends JpaRepository<ShirtDetail, Integer> {
    @Query("SELECT sd FROM ShirtDetail sd WHERE (sd.codeShirtDetail LIKE :query) OR (sd.shirt.nameShirt LIKE :query)")
    List<ShirtDetail> findListShirtDetailByCodeOrName(String query, Pageable pageable);

    @Query("SELECT sd FROM ShirtDetail sd WHERE sd.codeShirtDetail LIKE :query")
    ShirtDetail findShirtDetailByCode(String query);
}
