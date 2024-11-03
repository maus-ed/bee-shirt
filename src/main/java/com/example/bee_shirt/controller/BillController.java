package com.example.bee_shirt.controller;
import com.example.bee_shirt.dto.request.BillDTO;
import com.example.bee_shirt.dto.request.BillHistoryDTO;
import com.example.bee_shirt.entity.Bill;
import com.example.bee_shirt.entity.BillDetail;
import com.example.bee_shirt.entity.BillPayment;
import com.example.bee_shirt.repository.BillDetailrepo;
import com.example.bee_shirt.repository.BillPaymentRepo;
import com.example.bee_shirt.repository.BillRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class BillController {
    @Autowired
    BillRepo billrepo;
    @Autowired
    BillPaymentRepo billpaymentrepo;
    @Autowired
    BillDetailrepo billDetailrepo;
    @GetMapping("/billHistory/list")
    public List<BillHistoryDTO> getBillHistory() {
        List<Bill> bills = billrepo.findAll();
        System.out.println("Số lượng lịch sử hóa đơn: " + bills.size()); // Thêm log
        return bills.stream()
                .map(bill -> new BillHistoryDTO(bill.getCodeBill(), bill.getDesiredDate(), bill.getTotalMoney(), bill.getStatusBill()))
                .collect(Collectors.toList());
    }
    @GetMapping("/bill/list")
    public List<BillDTO> getBill() {
        List<BillDetail> bills = billDetailrepo.findAll();
        System.out.println("Số lượng hóa đơn: " + bills.size()); // Thêm log
        return bills.stream()
                .map(bill -> new BillDTO(bill.getBill().getCodeBill(), bill.getBill().getDesiredDate(),
                        bill.getBillpayment().getPaymentMethod().getNamePaymentMethod(), bill.getBill().getTotalMoney(),
                        bill.getBill().getStatusBill() ))
                .collect(Collectors.toList());
    }



}
