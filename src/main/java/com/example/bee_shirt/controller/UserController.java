package com.example.bee_shirt.controller;

import com.example.bee_shirt.dto.request.AccountCreationRequest;
import com.example.bee_shirt.dto.response.AccountResponse;
import com.example.bee_shirt.dto.response.ApiResponse;
import com.example.bee_shirt.service.AccountService;
import com.example.bee_shirt.service.RoleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AccountResponse>> register(@Valid @RequestBody AccountCreationRequest request) {
        AccountResponse accountResponse = accountService.createAccount(request);

        ApiResponse<AccountResponse> response = ApiResponse.<AccountResponse>builder()
                .code(1000)
                .result(accountResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
