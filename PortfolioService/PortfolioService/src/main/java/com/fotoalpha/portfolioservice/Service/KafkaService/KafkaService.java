package com.fotoalpha.portfolioservice.Service.KafkaService;


import com.fotoalpha.portfolioservice.Entity.Portfolio;
import com.fotoalpha.portfolioservice.KafkaEvents.SavePhotosEvent;
import com.fotoalpha.portfolioservice.Repository.PortfolioServiceDB;
import com.fotoalpha.portfolioservice.Types.PhotoType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {
    private final PortfolioServiceDB pfDb;

    @Transactional
    public void savePhotos(SavePhotosEvent event) {

        List<String> URLs = event.URLs();
        for (String url : URLs) {
            String photoType =  url.split("#")[0];
            String toSaveUrl = url.substring(url.indexOf("#") + 1);
            Portfolio newPortfolio =  Portfolio.builder()
                    .photo_url(toSaveUrl)
                    .photoType(photoType)
                    .build();
            pfDb.save(newPortfolio);
            log.info("Saved {} to DB", URLs);
        }
    }
}
