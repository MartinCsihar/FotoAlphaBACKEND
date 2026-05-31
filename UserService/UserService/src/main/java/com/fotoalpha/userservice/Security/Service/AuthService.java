package com.fotoalpha.userservice.Security.Service;

import com.fotoalpha.userservice.Entity.User;
import com.fotoalpha.userservice.Repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String login(String data, String rawPassword, boolean withUsername) {
        User user = null;
        if (withUsername) {
            user = userRepo.findByUserID(data).orElseThrow(() -> new RuntimeException("Invalid credentials"));
            if (passwordEncoder.matches(rawPassword,  user.getPassword()))  return jwtService.generateToken(user);
            else return "";
        }
        else if (!withUsername) {
            user = userRepo.findByEmail(data).orElseThrow(() -> new RuntimeException("Invalid credentials"));
            if (passwordEncoder.matches(rawPassword,  user.getPassword()))  return jwtService.generateToken(user);
            else return "";
        }
        return null;
    }
}
