package com.fotoalpha.userratingsservice.Service;

import com.fotoalpha.userratingsservice.Entity.URSKeys;
import com.fotoalpha.userratingsservice.Entity.UserRatingsService;
import com.fotoalpha.userratingsservice.Kafka.Producer;
import com.fotoalpha.userratingsservice.KafkaEvents.AppInfoReqEvent;
import com.fotoalpha.userratingsservice.KafkaEvents.AppInfoResEvent;
import com.fotoalpha.userratingsservice.Repo.URSRepo;
import com.fotoalpha.userratingsservice.RequestsResponses.AppointmentResponse;
import com.fotoalpha.userratingsservice.RequestsResponses.FetchAllRatingsResponse;
import com.fotoalpha.userratingsservice.RequestsResponses.RatingObject;
import com.fotoalpha.userratingsservice.RequestsResponses.SaveRatingRequest;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

    public FetchAllRatingsResponse getRatings() throws ExecutionException, InterruptedException, TimeoutException {
        List<String> appIds = ursRepo.getAllAppointmentIds();
        AppInfoReqEvent appInfoReqEvent = AppInfoReqEvent.builder()
                .correlationId(UUID.randomUUID().toString())
                .appIds(appIds)
                .build();
        AppInfoResEvent aire = producer.sendAppInfoReqEvent(appInfoReqEvent);

        List<RatingObject> ratings = new ArrayList<>();

        for (int i = 0; i < appIds.size(); i++) {
            URSKeys key = new URSKeys();
            key.setUserId(aire.querriedAppointments().get(i).getUserId());
            key.setAppointmentId(aire.querriedAppointments().get(i).getAppId());
            UserRatingsService userRating = ursRepo.findById(key).orElse(null);

            RatingObject newRating = RatingObject.builder()
                    .appointmentResponse(aire.querriedAppointments().get(i))
                    .rating(userRating.getRating())
                    .date(userRating.getDate())
                    .build();
            ratings.add(newRating);
        }
        return new FetchAllRatingsResponse(ratings);
    }
}
