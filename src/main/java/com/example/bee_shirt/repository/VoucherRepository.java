package com.example.bee_shirt.repository;

import com.example.bee_shirt.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    @Query("SELECT v FROM  Voucher v WHERE v.code_voucher = :code")
    Optional<Voucher> findByCode_voucher(@Param("code") String code);

    // Tìm tất cả voucher có enddate nhỏ hơn ngày hiện tại
    List<Voucher> findByEnddateBefore(LocalDate currentDate);

    @Query("SELECT v FROM Voucher v " +
            "WHERE LOWER(v.code_voucher) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(v.type_voucher) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(v.name_voucher) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR CAST(v.discount_value AS string) LIKE CONCAT('%', :keyword, '%') " +
            "OR CAST(v.quantity AS string) LIKE CONCAT('%', :keyword, '%') " +
            "OR CAST(v.min_bill_value AS string) LIKE CONCAT('%', :keyword, '%') " +
            "OR CAST(v.maximum_discount AS string) LIKE CONCAT('%', :keyword, '%') " +
            "OR LOWER(CAST(v.startdate AS string)) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(CAST(v.enddate AS string)) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR CAST(v.status_voucher AS string) LIKE CONCAT('%', :keyword, '%')")
    List<Voucher> searchAllFields(@Param("keyword") String keyword);

    @Query("SELECT v FROM Voucher v WHERE v.startdate >= :batdau AND v.enddate <= :ketthuc")
    List<Voucher> findByDateRange(@Param("batdau") LocalDate batdau, @Param("ketthuc") LocalDate ketthuc);
    


}
