package com.fotoalpha.userservice.Security;

import com.fotoalpha.userservice.Entity.User;
import com.fotoalpha.userservice.Repo.UserRepo;
import com.fotoalpha.userservice.Security.SecurityObjects.JwtAuthenticationFilter;
import com.fotoalpha.userservice.Security.Service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserRepo userRepo;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/health").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/admin-api/**").hasRole("ADMIN")
                        .requestMatchers("/test/**").authenticated()
                        .requestMatchers("/auth/**").permitAll()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            if (userRepo.count() == 0) {
                User user = User.builder()
                        .userID("MARTIN_ADMINISTRATOR")
                        .password(passwordEncoder().encode("Csiharmartin25hu"))
                        .email("martincsihar@gmail.com")
                        .phoneNumber("+36308602406")
                        .firstName("Csihar")
                        .lastName("Martin")
                        .role("ADMIN")
                        .build();
                userRepo.save(user);
            }
        };
    }
}
