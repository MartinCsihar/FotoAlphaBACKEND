package com.fotoalpha.portfolioservice.Entity;

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

    private UUID photo_id;

    @Column(name = "photo_url",columnDefinition = "TEXT")
    private String photo_url;

    private String photoType;

}
