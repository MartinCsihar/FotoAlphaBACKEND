package com.fotoalpha.userratingsservice.Service;

import com.fotoalpha.userratingsservice.Entity.URSKeys;
import com.fotoalpha.userratingsservice.Entity.UserRatingsService;
import com.fotoalpha.userratingsservice.Repo.URSRepo;
import com.fotoalpha.userratingsservice.RequestsResponses.SaveRatingRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class URSService {
    private static final Logger log = LoggerFactory.getLogger(URSService.class);
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
}
