package com.fotoalpha.userservice.Requests;

import lombok.Data;

@Data
public class RegisterRequest {
    String username;
    String email;
    String password;
    String firstName;
    String lastName;
    String phoneNumber;

}
