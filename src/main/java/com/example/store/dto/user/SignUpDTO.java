package com.example.store.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
