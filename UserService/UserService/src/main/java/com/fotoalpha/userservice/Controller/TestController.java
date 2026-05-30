package com.fotoalpha.userservice.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/getRole")
    public String getRole(Authentication authentication) {
        if (authentication == null) return "User not logged in!";
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        if (!role.isEmpty()) {
            return role;
        }
        return null;
    }
}
