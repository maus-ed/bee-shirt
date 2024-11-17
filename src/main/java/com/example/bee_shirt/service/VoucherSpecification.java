package com.example.bee_shirt.service;

import com.example.bee_shirt.entity.Voucher;
import org.springframework.data.jpa.domain.Specification;

public class VoucherSpecification {


    // Specification cho mã voucher
    public static Specification<Voucher> hasCodeVoucher(String codeVoucher) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("code_voucher"), "%" + codeVoucher + "%");
    }

    // Specification cho tên voucher
    public static Specification<Voucher> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name_voucher"), "%" + name + "%");
    }

    // Specification cho trạng thái voucher
    public static Specification<Voucher> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if ("Active".equalsIgnoreCase(status)) {
                return criteriaBuilder.equal(root.get("status_voucher"), 1);
            } else if ("Inactive".equalsIgnoreCase(status)) {
                return criteriaBuilder.equal(root.get("status_voucher"), 0);
            }
            return null;
        };
    }

    // Specification cho ngày bắt đầu
    public static Specification<Voucher> hasStartDate(String startdate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("startdate"), startdate);
    }

    // Specification cho ngày kết thúc
    public static Specification<Voucher> hasEndDate(String enddate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("enddate"), enddate);
    }




}
