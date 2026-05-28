package com.fotoalpha.userratingsservice.RequestsResponses;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SaveRatingRequest {
    String ratingText;
    int rating;
}
