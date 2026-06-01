package com.fotoalpha.userservice.RequestsResponses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetUser {
    String username;
    String fullName;
    String email;
    String phoneNumber;
}
