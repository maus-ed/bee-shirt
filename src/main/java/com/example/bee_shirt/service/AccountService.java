package com.example.bee_shirt.service;

import com.example.bee_shirt.dto.request.AccountCreationRequest;
import com.example.bee_shirt.dto.response.AccountResponse;
import com.example.bee_shirt.entity.Account;
import com.example.bee_shirt.exception.AppException;
import com.example.bee_shirt.exception.ErrorCode;
import com.example.bee_shirt.mapper.AccountMapper;
import com.example.bee_shirt.repository.AccountRepository;
import com.example.bee_shirt.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
//Thay thế cho @Autowired
//@RequiredArgsConstructor sẽ tự động tạo contructor của những method đc khai báo là final
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
//in ra log
@Slf4j
public class AccountService {
    AccountMapper accountMapper;

    AccountRepository accountRepository;

    RoleRepository roleRepository;

    // lấy thông tin người đang đăng nhập
    public AccountResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        Account account = accountRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return accountMapper.toUserResponse(account);
    }

    public List<AccountResponse> getAll(){
        List<Account> accounts = accountRepository.findAll();

        // Kiểm tra dữ liệu Role được nạp
        for (Account account : accounts) {
            System.out.println("Account: " + account.getUsername() + " - Roles: " + account.getRole());
        }

        return accounts.stream().map(accountMapper::toUserResponse).toList();
    }

    //Tạo account (admin)
    public AccountResponse createAccount(AccountCreationRequest request){
        if (accountRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Account account = accountMapper.toUser(request);
        //Mã hóa pass
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        account.setPass(passwordEncoder.encode(request.getPass()));
        //lấy ngày hiện tại
        LocalDate now = LocalDate.now();
        account.setCreateAt(now);

        //Lấy role theo id
        var roles = roleRepository.findAllById(request.getRole());

        account.setRole(new HashSet<>(roles));

        return accountMapper.toUserResponse(accountRepository.save(account));
    }
}
