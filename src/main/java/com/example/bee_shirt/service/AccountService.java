package com.example.bee_shirt.service;

import com.example.bee_shirt.dto.request.AccountCreationRequest;
import com.example.bee_shirt.dto.response.AccountResponse;
import com.example.bee_shirt.dto.response.RoleResponse;
import com.example.bee_shirt.entity.Account;
import com.example.bee_shirt.entity.Role;
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
import java.util.*;

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
    public AccountResponse createAccount(AccountCreationRequest request) {
        // Kiểm tra xem tài khoản với username "admin" đã tồn tại chưa
        if (request.getUsername().equals("admin") && accountRepository.findByUsername("admin").isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED); // Hoặc một mã lỗi phù hợp khác
        }

        // Tạo mã tài khoản tự động
        String generatedCode;

        // Nếu chưa có tài khoản nào, tạo mã đầu tiên
        if (accountRepository.getTop1() == null) {
            generatedCode = "ACC1";
        } else {
            // Lấy giá trị code đầu tiên
            String lastCode = accountRepository.getTop1().getCode();

            // Đảm bảo độ dài mã tài khoản đủ để cắt
            if (lastCode.length() > 3) {
                String prefix = lastCode.substring(0, 3); // 3 ký tự đầu
                int number = Integer.parseInt(lastCode.substring(3)); // Phần số sau
                generatedCode = prefix + (number + 1); // Tạo mã mới
            } else {
                // Nếu mã cũ quá ngắn, sử dụng mã mặc định
                generatedCode = "ACC1";
            }
        }

        // Thiết lập mã cho account
        request.setCode(generatedCode);

        // Kiểm tra xem tài khoản đã tồn tại chưa sau khi thiết lập mã
        if (accountRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Chuyển đổi request thành account
        Account account = accountMapper.toUser(request);
        System.out.println("Mã tài khoản mới: " + account.getCode());

        // Mã hóa pass
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        account.setPass(passwordEncoder.encode(request.getPass()));

        // Lấy thông tin tài khoản đang đăng nhập
        String code = this.getMyInfo().getCode();
        account.setCreateBy(code);

        // Lấy ngày hiện tại
        LocalDate now = LocalDate.now();
        account.setCreateAt(now);

        // Lấy role từ request
        Set<Role> roles = getRolesFromRequest(request.getRole());
        account.setRole(roles);

        // Lưu tài khoản vào database và trả về
        return accountMapper.toUserResponse(accountRepository.save(account));
    }



    // Phương thức riêng để lấy role từ request
    private Set<Role> getRolesFromRequest(List<String> roleCodes) {
        Set<Role> roles = new HashSet<>();

        // Nếu roleCodes null hoặc rỗng, gán role mặc định là "USER"
        if (roleCodes == null || roleCodes.isEmpty()) {
            Optional<Role> userRoleOptional = roleRepository.findRoleByCode("USER");
            if (userRoleOptional.isEmpty()) {
                throw new AppException(ErrorCode.ROLE_NOT_FOUND); // Nếu không tìm thấy role "USER"
            }
            roles.add(userRoleOptional.get());
        } else {
            // Lấy role từ danh sách roleCodes
            for (String roleCode : roleCodes) {
                Optional<Role> userRoleOptional = roleRepository.findRoleByCode(roleCode);
                if (userRoleOptional.isPresent()) {
                    roles.add(userRoleOptional.get());
                } else {
                    throw new AppException(ErrorCode.ROLE_NOT_FOUND);
                }
            }
        }

        return roles;
    }
}
