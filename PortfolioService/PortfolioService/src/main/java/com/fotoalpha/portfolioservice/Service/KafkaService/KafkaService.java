package com.fotoalpha.portfolioservice.Service.KafkaService;


import com.fotoalpha.portfolioservice.Entity.Portfolio;
import com.fotoalpha.portfolioservice.KafkaEvents.SavePhotosEvent;
import com.fotoalpha.portfolioservice.Repository.PortfolioServiceDB;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaService {
    private final PortfolioServiceDB pfDb;

    public void savePhotos(SavePhotosEvent event) {
        List<String> presignedURLs = event.presignedURLs();
        for (String url : presignedURLs) {
            Portfolio newPortfolio = Portfolio.builder()
                    .PhotoURL(url)
                    .build();
            pfDb.save(newPortfolio);
        }
    }
}
