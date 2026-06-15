package com.fotoalpha.userservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Users")
public class User {
    @Id
    String userID;
    String username;
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

    @PrePersist
    public void prePersist() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb_appId = new StringBuilder(chars.length());
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            sb_appId.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        userID = "#" + sb_appId.toString();
    }


}
