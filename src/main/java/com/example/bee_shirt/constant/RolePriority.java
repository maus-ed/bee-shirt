package com.example.bee_shirt.constant;

import java.util.Map;

public class RolePriority {
    public static final int ROLE_ADMIN = 3;
    public static final int ROLE_STAFF = 2;
    public static final int ROLE_USER = 1;

    public static final Map<String, Integer> rolePriorityMap = Map.of(
            "ROLE_ADMIN", ROLE_ADMIN,
            "ROLE_STAFF", ROLE_STAFF,
            "ROLE_USER", ROLE_USER
    );
}
