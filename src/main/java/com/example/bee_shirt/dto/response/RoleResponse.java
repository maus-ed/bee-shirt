package com.example.bee_shirt.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//Access ModiFier
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    Integer id;

    String code;

    String name;

    Integer status;

    String description;
}
