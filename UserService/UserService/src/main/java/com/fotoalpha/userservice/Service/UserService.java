package com.fotoalpha.userservice.Service;

import com.fotoalpha.userservice.Entity.PasswordResetToken;
import com.fotoalpha.userservice.Entity.User;
import com.fotoalpha.userservice.Kafka.Producer;
import com.fotoalpha.userservice.KafkaEvents.GalleryUpdatedEvent;
import com.fotoalpha.userservice.KafkaEvents.GetUserDataEvent;
import com.fotoalpha.userservice.KafkaEvents.GetUserDataEventResponse;
import com.fotoalpha.userservice.KafkaEvents.SendMailEvent;
import com.fotoalpha.userservice.Repo.PassowordResetTokenRepo;
import com.fotoalpha.userservice.Repo.UserRepo;
import com.fotoalpha.userservice.RequestsResponses.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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

    @Transactional
    public String modifyUserData(UserModifyDataRequest req, String uid) {
        User user = userRepo.findByUserID(uid)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with the given userid: " + uid));
        if (req.getFirstName() != null) {
            user.setFirstName(req.getFirstName());
        }
        if (req.getLastName() != null) {
            user.setLastName(req.getLastName());
        }
        if (req.getPhoneNum() != null) {
            user.setPhoneNumber(req.getPhoneNum());
        }
        if (req.getEmail() != null) {
            user.setEmail(req.getEmail());

        }
        userRepo.save(user);
        return  "Sikeres adat modosítás!";
    }

    public Integer numberOfUsers() {
        Integer count = userRepo.countDistinct();
        return count == null ? 0 : count;
    }

    public Map<String, Integer> numberOfPhotosVideos() {
        Integer sumPhotos = userRepo.countPhotos();
        Integer sumVideos = userRepo.countVideos();
        Map<String, Integer> map = new HashMap<>();
        map.put("photos", sumPhotos);
        map.put("videos", sumVideos);
        return map;

    }

    public Object getAllUsers() {
        List<GetUser> gu = userRepo.getUsers().stream().map(user -> {
            return GetUser.builder()
                    .username(user.getUserID())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .fullName(user.getLastName() + " " + user.getFirstName())
                    .build();
        }).toList();
        if(gu.isEmpty()) return "Nincsenek felhasználók!";
        return GetAllUsersResponse.builder()
                .users(gu)
                .build();
    }

    public Object getUser(String uid) {
        User user = userRepo.findByUserID(uid)
                .orElseThrow(() ->  new UsernameNotFoundException("User not found with the given userid: " + uid));
        GetUser gu = GetUser.builder()
                .username(user.getUserID())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .fullName( user.getFirstName() + " " + user.getLastName() )
                .build();
        return user != null ? gu : "Nincs ilyen felhasználó!";
    }


    @Transactional
    public Object saveNumOfPhotosAndOrVideosForUser(SaveCount req) {
        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() ->  new UsernameNotFoundException("User not found with the given email: " + req.getEmail()));
        user.setNumberOfPhotos(user.getNumberOfPhotos() + req.getPhotoCount());
        user.setNumberOfVideos(user.getNumberOfVideos() + req.getVideoCount());
        userRepo.save(user);
        GalleryUpdatedEvent gue = GalleryUpdatedEvent.builder()
                .firstname(user.getFirstName())
                .photoCount(req.getPhotoCount())
                .videoCount(req.getVideoCount())
                .email(req.getEmail())
                .build();
        producer.sendGalleryUpdatedEvent(gue);
        return "Sikeres mentés!";
    }
}
