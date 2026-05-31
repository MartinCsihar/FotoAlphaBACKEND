package com.fotoalpha.userservice.Service;

import com.fotoalpha.userservice.Entity.User;
import com.fotoalpha.userservice.Kafka.Producer;
import com.fotoalpha.userservice.KafkaEvents.GetUserDataEvent;
import com.fotoalpha.userservice.KafkaEvents.GetUserDataEventResponse;
import com.fotoalpha.userservice.Repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final Producer producer;

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
}
