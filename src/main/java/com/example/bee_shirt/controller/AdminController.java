package com.example.bee_shirt.controller;

import com.example.bee_shirt.dto.request.AccountCreationRequest;
import com.example.bee_shirt.dto.response.AccountResponse;
import com.example.bee_shirt.dto.response.ApiResponse;
import com.example.bee_shirt.dto.response.RoleResponse;
import com.example.bee_shirt.service.AccountService;
import com.example.bee_shirt.service.RoleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse<List<RoleResponse>> getRoles(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @GetMapping("/accounts")
    public ApiResponse<List<AccountResponse>> getAccounts(){
        return ApiResponse.<List<AccountResponse>>builder()
                .result(accountService.getAll())
                .build();
    }

    //thêm @Valid để create chạy validate
    @PostMapping("/create")
    public ApiResponse<AccountResponse> createUser(@RequestBody @Valid AccountCreationRequest request) {
        return ApiResponse.<AccountResponse>builder()
                .result(accountService.createAccount(request))
                .build();
    }
}
