package com.fotoalpha.userservice.Controller;

import com.fotoalpha.userservice.Kafka.Producer;
import com.fotoalpha.userservice.RequestsResponses.LoginRequest;
import com.fotoalpha.userservice.RequestsResponses.RegisterRequest;
import com.fotoalpha.userservice.RequestsResponses.UserPasswordResetReq;
import com.fotoalpha.userservice.Security.Service.AuthService;
import com.fotoalpha.userservice.Security.Service.RegistrationService;
import com.fotoalpha.userservice.RequestsResponses.SendMailRequest;
import com.fotoalpha.userservice.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final Producer producer;
    private final RegistrationService registrationService;
    private final AuthService authService;
    private final UserService userService;

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
        else if(req.getUsername() == null) {
            jwt = authService.login(req.getEmail(), req.getPassword(), false);
        }
        if(jwt.isEmpty()) {
            return ResponseEntity.badRequest().body("Hibás adat!");
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
    @PostMapping("/passwordResetEmail")
    public ResponseEntity<?> sendMailForPwReset(@RequestBody SendMailRequest req) {
        return new ResponseEntity<>(userService.sendPwResetMailEvent(req), HttpStatus.OK);
    }

    @PostMapping("/passwordReset")
    public ResponseEntity<?> resetPassword(@RequestBody UserPasswordResetReq req){
        return new ResponseEntity<>(userService.resetPassword(req), HttpStatus.OK);
    }
    @GetMapping("/isLoggedIn")
    public boolean isLoggedIn(Authentication auth) {
        return auth != null;
    }
}
