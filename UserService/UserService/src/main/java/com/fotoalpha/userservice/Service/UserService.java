package com.fotoalpha.userservice.Service;

import com.fotoalpha.userservice.Entity.PasswordResetToken;
import com.fotoalpha.userservice.Entity.User;
import com.fotoalpha.userservice.Kafka.Producer;
import com.fotoalpha.userservice.KafkaEvents.GetUserDataEvent;
import com.fotoalpha.userservice.KafkaEvents.GetUserDataEventResponse;
import com.fotoalpha.userservice.KafkaEvents.SendMailEvent;
import com.fotoalpha.userservice.Repo.PassowordResetTokenRepo;
import com.fotoalpha.userservice.Repo.UserRepo;
import com.fotoalpha.userservice.Requests.SendMailRequest;
import com.fotoalpha.userservice.Requests.UserPasswordResetReq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final Producer producer;
    private final PassowordResetTokenRepo passowordResetTokenRepo;
    private final PasswordEncoder passwordEncoder;

    public void createGetUserDataResponse(GetUserDataEvent event) {
        User user = userRepo.findByUserID(event.UserID())
                .orElseThrow(()-> new UsernameNotFoundException("User not found with the given userid: " + event.UserID()));
        GetUserDataEventResponse res = GetUserDataEventResponse.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .correlationId(event.correlationId())
                .build();

        producer.sendGUDER(res);
    }
    public Object sendPwResetMailEvent(SendMailRequest req) {
        if (userRepo.findByEmail(req.getEmail()).isEmpty()) {
            return "Még nem létezik fiók ezzel az email címmel!";
        }
        Map<String, Object> res = new HashMap<>();
        String token = UUID.randomUUID().toString();

        PasswordResetToken pToken = PasswordResetToken.builder()
                .token(token)
                .used(false)
                .email(req.getEmail())
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .build();
        passowordResetTokenRepo.save(pToken);

        res.put("message", "Visszaállító email sikeresen elküldve!");

        SendMailEvent event = new SendMailEvent(req.getEmail(), pToken.getToken());
        producer.sendPwResetEmailEvent(event);

        return res;
    }
    public Object resetPassword(UserPasswordResetReq req){
        String token = req.getToken();
        if(passowordResetTokenRepo.findById(token).isEmpty()) {
            return "Nincs engedélyed megváltoztatni a jelszót!";
        }
        else if(passowordResetTokenRepo.findById(token).get().getExpiryDate().isBefore(LocalDateTime.now())) {
            passowordResetTokenRepo.deleteById(token);
            return "A próbálkozás lejárt, próbáld meg újból!";
        }
        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("User not found with the given email: " + req.getEmail()));
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepo.save(user);
        passowordResetTokenRepo.deleteById(token);
        return "Sikeresen megváltoztattad a jelszavad! Most visszairányítalak a belenetkezéshez!";
    }
}
