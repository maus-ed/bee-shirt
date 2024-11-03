package com.example.bee_shirt.controller;

import com.example.bee_shirt.dto.request.BillDetailDTO;
import com.example.bee_shirt.service.BillDetailService; // Sử dụng service thay vì repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class BillDetailController {

    private final BillDetailService billDetailService; // Chỉnh sửa ở đây để sử dụng service

    @Autowired
    public BillDetailController(BillDetailService billDetailService) { // Sửa ở đây
        this.billDetailService = billDetailService;
    }

    @GetMapping("/bill/details/{codeBill}")
    public List<BillDetailDTO> getBillDetail(@PathVariable String codeBill) {
        return billDetailService.getBillDetails(codeBill); // Gọi phương thức từ service
    }
}
