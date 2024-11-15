package com.example.bee_shirt.service;

import com.example.bee_shirt.dto.request.BillDTO;
import com.example.bee_shirt.repository.BillRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BillService {

    BillRepo billRepository;

    public List<BillDTO> getAllBillSummaries() {
        List<Object[]> results = billRepository.findBillSummaryNative();

        return results.stream().map(result -> new BillDTO(
                (String) result[0],                      // codeBill
                (String) result[1],                      // customerName
                result[2] != null ? ((Date) result[2]).toLocalDate() : null,   // desiredDate, convert sql Date to LocalDate
                (String) result[3],                      // namePaymentMethod
                (BigDecimal) result[4],                  // totalMoney
                (Integer) result[5]                      // statusBill
        )).collect(Collectors.toList());
    }
}
