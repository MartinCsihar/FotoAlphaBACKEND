package com.fotoalpha.userservice.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserModifyDataRequest {
    String email;
    String firstName;
    String lastName;
    String phoneNum;
}
