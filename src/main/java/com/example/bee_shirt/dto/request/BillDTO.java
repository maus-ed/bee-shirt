package com.example.bee_shirt.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BillDTO {


    private String codeBill;
    private LocalDate desiredDate;
    private String namePaymentMethod;
    private BigDecimal totalMoney;
    private Integer statusBill;

    // Constructor, getters và setters
    public BillDTO(String codeBill, LocalDate desiredDate,String namePaymentMethod, BigDecimal totalMoney, Integer statusBill) {
        this.codeBill = codeBill;
        this.desiredDate = desiredDate;
        this.namePaymentMethod = namePaymentMethod;
        this.totalMoney = totalMoney;
        this.statusBill = statusBill;
    }

}
