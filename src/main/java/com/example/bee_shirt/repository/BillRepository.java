package com.example.bee_shirt.repository;

import com.example.bee_shirt.entity.Bill;
import com.example.bee_shirt.entity.ShirtDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Integer> {
    @Query("SELECT b FROM Bill b WHERE b.codeBill LIKE :query")
    Bill findBillByCode(String query);

    @Query("SELECT b FROM Bill b WHERE b.statusBill = 0")
    List<Bill> findPendingBill();
}
