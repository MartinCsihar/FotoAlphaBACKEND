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
    private UUID PhotoID;
    // presigned url. Comes from AwsService
    private String PhotoURL;
}
