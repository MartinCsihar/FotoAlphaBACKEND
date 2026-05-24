package com.fotoalpha.appointmentsservice.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    String userId;
    Integer numberOfPhotos;
    String email;
    String firstName;
    String lastName;
    String phoneNumber;
}
