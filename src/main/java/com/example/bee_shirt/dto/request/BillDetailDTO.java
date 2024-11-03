package com.example.bee_shirt.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data

public class BillDetailDTO {
    private String nameImage;
    private String nameShirt;
    private String nameBrand;
    private String nameSize;
    private int quantity;
    private double price;
    private String nameVoucher;
    private double subtotalBeforeDiscount;
    private double totalMoney;

    // Constructor
    public BillDetailDTO(String nameImage, String nameShirt, String nameBrand, String nameSize,
                         int quantity, double price, String nameVoucher,
                         double subtotalBeforeDiscount, double totalMoney) {
        this.nameImage = nameImage;
        this.nameShirt = nameShirt;
        this.nameBrand = nameBrand;
        this.nameSize = nameSize;
        this.quantity = quantity;
        this.price = price;
        this.nameVoucher = nameVoucher;
        this.subtotalBeforeDiscount = subtotalBeforeDiscount;
        this.totalMoney = totalMoney;
    }
}