package com.fotoalpha.userratingsservice.Entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserRatingsService {
    @EmbeddedId
    URSKeys id;
    int rating;
    LocalDate date;
    String ratingText;
}
