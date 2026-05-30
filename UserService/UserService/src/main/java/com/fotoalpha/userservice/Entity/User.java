package com.fotoalpha.userservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Users")
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
