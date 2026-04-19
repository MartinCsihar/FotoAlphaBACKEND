package com.fotoalpha.portfolioservice.Entity;

import com.fotoalpha.portfolioservice.Types.PhotoType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID PhotoID;
    @Enumerated(EnumType.STRING)
    private PhotoType type;
    // presigned url. Comes from AwsService
    private String PhotoURL;
}
