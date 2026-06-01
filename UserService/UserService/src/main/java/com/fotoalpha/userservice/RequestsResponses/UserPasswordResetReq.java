package com.fotoalpha.userservice.RequestsResponses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordResetReq {
    String email;
    String password;
    String token;
}
