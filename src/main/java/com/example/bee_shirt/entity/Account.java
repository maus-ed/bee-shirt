package com.example.bee_shirt.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//Access ModiFier
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "createat")
    LocalDate createAt;
    @Column(name = "updateat")
    LocalDate updateAt;
    @Column(name = "status_account")
    Integer status;
    @Column(name = "code_account")
    String code;
    @Column(name = "username")
    String username;
    @Column(name = "pass")
    String pass;
    @Column(name = "first_name")
    String firstName;
    @Column(name = "last_name")
    String lastName;
    @Column(name = "avatar")
    String avatar;
    @Column(name = "address_account")
    String address;
    @Column(name = "phone_number")
    String phone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "account_role",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Role> role;

}
