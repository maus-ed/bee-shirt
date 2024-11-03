package com.example.bee_shirt.service;

import com.example.bee_shirt.dto.request.BillDetailDTO;
import com.example.bee_shirt.repository.BillDetailrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal; // Thêm import cho BigDecimal
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillDetailService {

    @Autowired
    private BillDetailrepo billDetailRepo;

    public List<BillDetailDTO> getBillDetails(String codeBill) {
        List<Object[]> results = billDetailRepo.findBillDetailsWithImageAndVoucherNative(codeBill);
        return results.stream().map(result -> new BillDetailDTO(
                (String) result[0],                      // Chuyển đổi string
                (String) result[1],                      // Chuyển đổi string
                (String) result[2],                      // Chuyển đổi string
                (String) result[3],                      // Chuyển đổi string
                (Integer) result[4],                     // Chuyển đổi Integer
                ((BigDecimal) result[5]).doubleValue(),  // Chuyển đổi BigDecimal sang double
                (String) result[6],                      // Chuyển đổi string
                ((BigDecimal) result[7]).doubleValue(),  // Chuyển đổi BigDecimal sang double
                ((BigDecimal) result[8]).doubleValue()   // Chuyển đổi BigDecimal sang double
        )).collect(Collectors.toList());
    }
}
