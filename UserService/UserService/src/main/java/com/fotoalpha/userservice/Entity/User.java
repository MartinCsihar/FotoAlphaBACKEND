package com.fotoalpha.userservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
    @Id
    String userID;
    Integer numberOfPhotos;
    String email;
    String password;
    String firstName;
    String lastName;
    String phoneNumber;
    String role;



}
