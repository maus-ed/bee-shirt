package com.example.bee_shirt.controller;

import com.example.bee_shirt.dto.request.BillDTO;
import com.example.bee_shirt.dto.request.BillHistoryDTO;
import com.example.bee_shirt.dto.response.ApiResponse;
import com.example.bee_shirt.entity.Bill;
import com.example.bee_shirt.entity.BillDetail;
import com.example.bee_shirt.repository.BillDetailrepo;
import com.example.bee_shirt.repository.BillPaymentRepo;
import com.example.bee_shirt.repository.BillRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("bills") // Đường dẫn theo kiểu RESTful
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BillController {

    BillRepo billrepo;
    BillPaymentRepo billpaymentrepo;
    BillDetailrepo billDetailrepo;

    @GetMapping("/history") // Đổi đường dẫn cho lịch sử hóa đơn
    public ApiResponse<List<BillHistoryDTO>> getBillHistory() {
        List<Bill> bills = billrepo.findAll();
        log.info("Số lượng lịch sử hóa đơn: {}", bills.size()); // Thêm log
        List<BillHistoryDTO> response = bills.stream()
                .map(bill -> new BillHistoryDTO(bill.getCodeBill(), bill.getDesiredDate(), bill.getTotalMoney(), bill.getStatusBill()))
                .collect(Collectors.toList());
        return ApiResponse.<List<BillHistoryDTO>>builder()
                .code(1000)
                .result(response)
                .build();
    }

    @GetMapping("/list") // Đổi đường dẫn cho danh sách hóa đơn
    public ApiResponse<List<BillDTO>> getBill() {
        List<BillDetail> bills = billDetailrepo.findAll();
        log.info("Số lượng hóa đơn: {}", bills.size()); // Thêm log
        List<BillDTO> response = bills.stream()
                .map(bill -> new BillDTO(bill.getBill().getCodeBill(),bill.getBill().getCustomerName(), bill.getBill().getDesiredDate(),
                        bill.getBillpayment().getPaymentMethod().getNamePaymentMethod(), bill.getBill().getTotalMoney(),
                        bill.getBill().getStatusBill()))
                .collect(Collectors.toList());
        return ApiResponse.<List<BillDTO>>builder()
                .code(1000)
                .result(response)
                .build();
    }

    // Thêm phương thức để xóa hóa đơn theo mã (Sử dụng String cho mã hóa đơn)
//    @DeleteMapping("/delete/{code}")
//    public ResponseEntity<ApiResponse<Void>> deleteBill(@PathVariable("code") String code) {
//        try {
//            // Giả sử có phương thức xóa trong BillRepo với mã hóa đơn là String
//            billrepo.deleteById(code);
//            return ResponseEntity.ok(
//                    ApiResponse.<Void>builder()
//                            .message("Bill deleted successfully.")
//                            .build()
//            );
//        } catch (AppException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(ApiResponse.<Void>builder()
//                            .message("Bill not found.")
//                            .build()
//                    );
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(ApiResponse.<Void>builder()
//                            .message("An error occurred while deleting the bill.")
//                            .build()
//                    );
//        }
//    }
}
