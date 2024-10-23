package com.example.bee_shirt.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//Access ModiFier
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountCreationRequest {
    String code;
    @NotEmpty(message = "FIRST NAME NOT EMPTY")
    String firstName;
    @NotEmpty(message = "LAST NAME NOT EMPTY")
    String lastName;

    String avatar;

    String address;
    @NotEmpty(message = "PHONE NOT EMPTY")
    String phone;

    Integer status;

    @Size(min = 3,message = "USERNAME_INVALID")
    String username;

    //validate password
    @Size(min = 5,message = "PASSWORD_INVALID")
    String pass;

    LocalDate createAt;

    String createBy;

    List<String> role;
}
