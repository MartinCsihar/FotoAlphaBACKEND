package com.fotoalpha.userservice.Security.Service;

import com.fotoalpha.userservice.Entity.User;
import com.fotoalpha.userservice.Repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with the given email: " + email));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserID())
                .password(user.getPassword())
                .roles(user.getRole().replace("ROLE_USER", "USER"))
                .build();
    }
}
