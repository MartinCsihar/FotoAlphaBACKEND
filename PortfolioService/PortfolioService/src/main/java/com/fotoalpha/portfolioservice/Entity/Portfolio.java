package com.fotoalpha.portfolioservice.Entity;

import com.fotoalpha.portfolioservice.Types.PhotoType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "photo_id")
    private UUID photo_id;
    // presigned url. Comes from AwsService
    @Column(name = "photo_url",columnDefinition = "TEXT")
    private String photo_url;
}
