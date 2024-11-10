package com.example.bee_shirt.dto.response;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//Access ModiFier
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    Integer id;

    String code;

    String firstName;

    String lastName;

    String avatar;

    String address;

    String phone;

    Integer status;

    LocalDate updateAt;

    String username;

    LocalDate createAt;

    String createBy;

    String updateBy;

    String email;

<<<<<<< HEAD
=======
    String deleted;

>>>>>>> 8e525c1d04e8811245e54faa619af4494760a40c
    Set<RoleResponse> role;
}
