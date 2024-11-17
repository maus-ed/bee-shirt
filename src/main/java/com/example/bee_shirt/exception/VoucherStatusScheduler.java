package com.example.bee_shirt.exception;


import com.example.bee_shirt.repository.VoucherRepository;
import com.example.bee_shirt.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class VoucherStatusScheduler {
    @Autowired

    private final VoucherService voucherService;

    public VoucherStatusScheduler(VoucherService voucherService, VoucherRepository voucherRepository) {
        this.voucherService = voucherService;
    }

    // Tự động kiểm tra và cập nhật trạng thái các voucher hết hạn vào mỗi ngày
    @Scheduled(cron = "0 0 0 * * ?")  // Chạy vào lúc 00:00 mỗi ngày
    public void updateExpiredVouchers() {
        voucherService.updateVoucherStatusBasedOnExpirationDate();
    }



}
