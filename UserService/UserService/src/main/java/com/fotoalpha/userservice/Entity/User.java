package com.fotoalpha.userservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    Integer numberOfVideos;
    String email;
    String password;
    String firstName;
    String lastName;
    String phoneNumber;
    String role;
    String key;
    LocalDateTime lastModified;



}
