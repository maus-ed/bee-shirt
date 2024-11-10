package com.example.bee_shirt.controller;

import com.example.bee_shirt.dto.request.AccountCreationRequest;
import com.example.bee_shirt.dto.response.AccountResponse;
import com.example.bee_shirt.dto.response.ApiResponse;
import com.example.bee_shirt.dto.response.RoleResponse;
import com.example.bee_shirt.exception.AppException;
import com.example.bee_shirt.service.AccountService;
import com.example.bee_shirt.service.RoleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<List<RoleResponse>> getRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .code(1000)
                .result(roleService.getAll())
                .build();
    }

    @GetMapping("/accounts")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<List<AccountResponse>> getAccounts() {
        return ApiResponse.<List<AccountResponse>>builder()
                .code(1000)
                .result(accountService.getAll())
                .build();
    }

    @GetMapping("/staffs")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<List<AccountResponse>> getStaffs() {
        return ApiResponse.<List<AccountResponse>>builder()
                .code(1000)
                .result(accountService.getAllStaff())
                .build();
    }

    @GetMapping("/clients")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<List<AccountResponse>> getClients() {
        return ApiResponse.<List<AccountResponse>>builder()
                .code(1000)
                .result(accountService.getAllClient())
                .build();
    }

    //thêm @Valid để create chạy validate
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<AccountResponse> createUser(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestParam("pass") String pass,
            @RequestParam("address") String address,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
            @RequestParam("role") List<String> roles) {

        // Tạo một đối tượng AccountCreationRequest từ các tham số
        AccountCreationRequest request = new AccountCreationRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setPhone(phone);
        request.setEmail(email);
        request.setUsername(username);
        request.setPass(pass);
        request.setAddress(address);
        request.setAvatarFile(avatarFile); // Đảm bảo rằng avatarFile được lưu trữ đúng cách
        request.setRole(roles); // Đặt các vai trò

        log.info("Received account creation request: {}", request);

        return ApiResponse.<AccountResponse>builder()
                .code(1000)
                .result(accountService.createAccount(request, true))
                .build();
    }

    // Endpoint để xoá tài khoản theo mã code
    @DeleteMapping("/delete/{code}")
    public ResponseEntity<ApiResponse<AccountResponse>> deleteUser(@PathVariable("code") String code) {
        try {
            AccountResponse deletedAccount = accountService.deleteAccount(code);
            return ResponseEntity.ok(
                    ApiResponse.<AccountResponse>builder()
                            .result(deletedAccount)
                            .message("Account deleted successfully.")
                            .build()
            );
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<AccountResponse>builder()
                            .message("Account not found.")
                            .build()
                    );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<AccountResponse>builder()
                            .message("An error occurred while deleting the account.")
                            .build()
                    );
        }
    }

}
