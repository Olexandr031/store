package com.example.store.dto.user;

import com.example.store.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCreateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private String password;
}
