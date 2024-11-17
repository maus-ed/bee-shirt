package com.example.bee_shirt.service;

import com.example.bee_shirt.entity.Voucher;
import com.example.bee_shirt.repository.VoucherRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
public class VoucherService {
    @Autowired
    private final VoucherRepository voucherRepository;
    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }
    public Page<Voucher> getAllVouchers(Pageable pageable) {
        // Tạo Pageable với sắp xếp theo id giảm dần
        Pageable sortedByIdDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("id")));

        // Trả về danh sách voucher với phân trang và sắp xếp
        return voucherRepository.findAll(sortedByIdDesc);
    }


    public List<Voucher> searchVouchers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return voucherRepository.findAll();
        }
        return voucherRepository.searchAllFields(keyword.trim());
    }


// search theo ngày tháng
    public List<Voucher> searchByDateRange(LocalDate batdau, LocalDate ketthuc) {
        if (batdau == null && ketthuc == null) {
            return voucherRepository.findAll();
        }
        if (batdau == null) {
            return voucherRepository.findByEnddate(ketthuc.plusDays(1)); // Thêm 1 ngày để bao gồm ngày kết thúc
        }
        if (ketthuc == null) {
            return voucherRepository.findByStartdateAfter(batdau.minusDays(1)); // Bao gồm ngày bắt đầu
        }
        return voucherRepository.findByStartdateAfterAndEnddateBefore(
                batdau.minusDays(1), ketthuc.plusDays(1)); // Bao gồm khoảng ngày
    }


    @Scheduled(fixedRate = 300000) // Chạy mỗi 5 phút (300000 ms)
    public void updateVoucherStatusBasedOnExpirationDate() {
        // Lấy ngày hiện tại
        LocalDate currentDate = LocalDate.now();

        // Lấy danh sách các voucher
        Iterable<Voucher> vouchers = voucherRepository.findAll();

        // Duyệt qua tất cả các voucher và kiểm tra ngày hết hạn
        for (Voucher voucher : vouchers) {
            // Nếu voucher có enddate và ngày hiện tại lớn hơn enddate
            if (voucher.getEnddate().isBefore(currentDate)) {
                voucher.setStatus_voucher(0); // Đặt trạng thái là 0 (Không hoạt động)
                voucherRepository.save(voucher); // Lưu voucher đã cập nhật vào cơ sở dữ liệu
            }

        }
    }

    public Optional<Voucher> getVoucherById(Long id) {
        return voucherRepository.findById(id);
    }
    public Optional<Voucher> getVoucherByCode(String code_voucher) {
        return voucherRepository.findByCode_voucher(code_voucher);
    }
    public Voucher saveVoucher(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    public void deleteVoucher(Long id) {
        voucherRepository.deleteById(id);
    }




}
