package com.example.bee_shirt.controller;

import com.example.bee_shirt.entity.Voucher;
import com.example.bee_shirt.repository.VoucherRepository;
import com.example.bee_shirt.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/voucher")
@CrossOrigin(origins = "http://127.0.0.1:5501")

public class VoucherController {
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private VoucherRepository voucherRepository;


    @GetMapping("/list")
    public ResponseEntity<Page<Voucher>> getAllVouchers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        // Tạo Pageable từ các tham số yêu cầu
        Pageable pageable = PageRequest.of(page, size);

        // Gọi service để lấy danh sách voucher có phân trang và sắp xếp
        Page<Voucher> vouchers = voucherService.getAllVouchers(pageable);

        return ResponseEntity.ok(vouchers);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Voucher>> searchVouchers(@RequestParam("keyword") String keyword) {
        List<Voucher> vouchers = voucherService.searchVouchers(keyword);
        return ResponseEntity.ok(vouchers);
    }

    @GetMapping("/searchByDateRange")
    public ResponseEntity<List<Voucher>> searchByDateRange(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate batdau,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ketthuc) {

        List<Voucher> vouchers = voucherService.searchByDateRange(batdau, ketthuc);
        return ResponseEntity.ok(vouchers);
    }

    @GetMapping("/detail/{code_voucher}")
    public ResponseEntity<Voucher> getVoucherByCode(@PathVariable String code_voucher) {
        Optional<Voucher> voucher = voucherService.getVoucherByCode(code_voucher);
        return voucher.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/detaill/{id}")
    public ResponseEntity<Voucher> getVoucherById(@PathVariable Long id) {
        Optional<Voucher> voucher = voucherService.getVoucherById(id);
        return voucher.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public Voucher addVoucher(@RequestBody Voucher voucher) {
        // Khi gửi yêu cầu, code_voucher sẽ tự động được tạo ra ở phía server.
        return voucherService.saveVoucher(voucher);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Voucher> updateVoucher(@PathVariable Long id, @RequestBody Voucher voucherDetails) {
        // Tìm voucher theo ID
        Optional<Voucher> voucherOpt = voucherService.getVoucherById(id);

        if (voucherOpt.isPresent()) {
            // Lấy voucher cũ từ cơ sở dữ liệu
            Voucher existingVoucher = voucherOpt.get();

            // Cập nhật các trường của voucher
            existingVoucher.setCode_voucher(voucherDetails.getCode_voucher());
            existingVoucher.setName_voucher(voucherDetails.getName_voucher());
            existingVoucher.setType_voucher(voucherDetails.getType_voucher());
            existingVoucher.setDiscount_value(voucherDetails.getDiscount_value());
            existingVoucher.setQuantity(voucherDetails.getQuantity());
            existingVoucher.setMin_bill_value(voucherDetails.getMin_bill_value());
            existingVoucher.setMaximum_discount(voucherDetails.getMaximum_discount());
            existingVoucher.setStartdate(voucherDetails.getStartdate());
            existingVoucher.setEnddate(voucherDetails.getEnddate());
            existingVoucher.setStatus_voucher(voucherDetails.getStatus_voucher());
            existingVoucher.setDescription_voucher(voucherDetails.getDescription_voucher());
            existingVoucher.setUpdateAt(voucherDetails.getUpdateAt());

            // Lưu voucher đã cập nhật
            Voucher updatedVoucher = voucherService.saveVoucher(existingVoucher);

            // Trả về voucher đã cập nhật
            return ResponseEntity.ok(updatedVoucher);
        }

        // Nếu không tìm thấy voucher, trả về lỗi 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVoucher(@PathVariable Long id) {
        if (voucherService.getVoucherById(id).isPresent()) {
            voucherService.deleteVoucher(id);
            return ResponseEntity.ok("Voucher đã được xóa thành công!");  // Thêm thông báo


            // Trả về 200 OK khi xóa thành công
        }
        return ResponseEntity.notFound().build();  // Trả về 404 nếu không tìm thấy voucher
    }


    // API để cập nhật tất cả các voucher đã hết hạn
    @PutMapping("/update-expired-status")
    public ResponseEntity<?> updateExpiredVouchers() {
        try {
            LocalDate currentDate = LocalDate.now();
            List<Voucher> vouchers = voucherRepository.findByEnddateBefore(currentDate);

            for (Voucher voucher : vouchers) {
                voucher.setStatus_voucher(0);  // Đặt trạng thái thành không hoạt động (Inactive)
                voucherRepository.save(voucher);
            }

            return ResponseEntity.ok("Expired voucher statuses updated.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating expired vouchers: " + e.getMessage());
        }
    }


//    @GetMapping("/search")
//    public List<Voucher> searchVouchers(@RequestParam(value = "name", required = false) String name_voucher,
//                                        @RequestParam(value = "code", required = false) String code_voucher) {
//        if (name_voucher != null) {
//            return voucherService.searchVouchersByName(name_voucher);
//        } else if (code_voucher != null) {
//            return voucherService.searchVouchersByCode(code_voucher);
//        }
//        return voucherService.getAllVouchers();
//    }
}
