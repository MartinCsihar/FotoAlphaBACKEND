package com.fotoalpha.userservice.Controller;

import com.fotoalpha.userservice.Entity.User;
import com.fotoalpha.userservice.Requests.LoginRequest;
import com.fotoalpha.userservice.Requests.RegisterRequest;
import com.fotoalpha.userservice.Security.Service.AuthService;
import com.fotoalpha.userservice.Security.Service.RegistrationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RegistrationService registrationService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest req) {
        registrationService.register(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req, HttpServletResponse response) {
        String jwt = "";
        if (req.getEmail() == null) {
             jwt = authService.login(req.getUsername(), req.getPassword(), true);
        }
        if(req.getUsername() == null) {
            jwt = authService.login(req.getEmail(), req.getPassword(), false);
        }
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt",  null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "Sikeres kijelentkezés!";
    }
}
