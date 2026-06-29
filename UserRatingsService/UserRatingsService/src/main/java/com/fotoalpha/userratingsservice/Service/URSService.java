package com.fotoalpha.userratingsservice.Service;

import com.fotoalpha.userratingsservice.Entity.URSKeys;
import com.fotoalpha.userratingsservice.Entity.UserRatingsService;
import com.fotoalpha.userratingsservice.Kafka.Producer;
import com.fotoalpha.userratingsservice.KafkaEvents.AppInfoReqEvent;
import com.fotoalpha.userratingsservice.KafkaEvents.AppInfoResEvent;
import com.fotoalpha.userratingsservice.KafkaEvents.UserInfoReqEvent;
import com.fotoalpha.userratingsservice.KafkaEvents.UserInfoResEvent;
import com.fotoalpha.userratingsservice.Repo.URSRepo;
import com.fotoalpha.userratingsservice.RequestsResponses.AppointmentResponse;
import com.fotoalpha.userratingsservice.RequestsResponses.FetchAllRatingsResponse;
import com.fotoalpha.userratingsservice.RequestsResponses.RatingObject;
import com.fotoalpha.userratingsservice.RequestsResponses.SaveRatingRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class URSService {
    private final Producer producer;
    private final URSRepo ursRepo;

    public Integer getNumberOfRatings() {
        return ursRepo.findAll().size();
    }

    public String getAvgRating() {
        double sum = 0;
        List<UserRatingsService> ratings = ursRepo.findAll();
        for (UserRatingsService rating : ratings) {
            sum += rating.getRating();
        }
        return String.format("%.1f", sum / ratings.size());
    }

    public Boolean saveRating(SaveRatingRequest req, String uid, String appid) {
        URSKeys key = new URSKeys();
        key.setUserId(uid);
        key.setAppointmentId(appid);
        UserRatingsService userRatingsService = UserRatingsService.builder()
                .id(key)
                .rating(req.getRating())
                .ratingText(req.getRatingText())
                .date(LocalDate.now())
                .build();
        try {
            ursRepo.save(userRatingsService);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Object getRatings() throws ExecutionException, InterruptedException, TimeoutException {

        List<String> appIds = ursRepo.getAllAppointmentIds();
        List<String> userIds = ursRepo.getAllUserIds();

        if (appIds.isEmpty()) {
            return "Még nincs értékelés! Legyél az első! :)";
        }

        UserInfoReqEvent userInfoReqEvent = UserInfoReqEvent.builder()
                .correlationId(UUID.randomUUID().toString())
                .userIDs(userIds)
                .build();

        AppInfoReqEvent appInfoReqEvent = AppInfoReqEvent.builder()
                .correlationId(UUID.randomUUID().toString())
                .appIds(appIds)
                .build();

        UserInfoResEvent userInfoResEvent = producer.sendUserInfoReqEvent(userInfoReqEvent);
        AppInfoResEvent appInfoResEvent = producer.sendAppInfoReqEvent(appInfoReqEvent);

        // userId -> index a user response-ban
        Map<String, Integer> userIndexMap = new HashMap<>();
        for (int i = 0; i < userInfoResEvent.userIds().size(); i++) {
            userIndexMap.put(userInfoResEvent.userIds().get(i), i);
        }

        List<RatingObject> ratings = new ArrayList<>();

        for (AppointmentResponse app : appInfoResEvent.querriedAppointments()) {

            URSKeys key = new URSKeys();
            key.setUserId(app.getUserId());
            key.setAppointmentId(app.getAppId());

            UserRatingsService userRating = ursRepo.findById(key).orElse(null);

            if (userRating == null) {
                continue;
            }

            int idx = userIndexMap.get(app.getUserId());

            ratings.add(
                    RatingObject.builder()
                            .appointmentResponse(app)
                            .rating(userRating.getRating())
                            .date(userRating.getDate())
                            .ratingText(userRating.getRatingText())
                            .userName(userInfoResEvent.userNames().get(idx))
                            .profilePicture(userInfoResEvent.profPicUrls().get(idx))
                            .build()
            );
        }

        return new FetchAllRatingsResponse(ratings);
    }
}
