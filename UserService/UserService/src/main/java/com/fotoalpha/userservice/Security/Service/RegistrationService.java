package com.fotoalpha.userservice.Security.Service;

import com.fotoalpha.userservice.Entity.User;
import com.fotoalpha.userservice.Repo.UserRepo;
import com.fotoalpha.userservice.RequestsResponses.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepo userRepo;
    private final PasswordEncoder encoder;

    public void register(RegisterRequest req) {
        if (userRepo.findByUserID(req.getUsername()).isPresent()) {
            throw new SecurityException("Email already exists");
        }
        User user = User.builder()
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .phoneNumber(req.getPhoneNumber())
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .phoneNumber(req.getPhoneNumber())
                .userID(req.getUsername())
                .numberOfPhotos(0)
                .numberOfVideos(0)
                .profilePictureUrl("")
                .role("USER")
                .build();
        userRepo.save(user);
    }
}
