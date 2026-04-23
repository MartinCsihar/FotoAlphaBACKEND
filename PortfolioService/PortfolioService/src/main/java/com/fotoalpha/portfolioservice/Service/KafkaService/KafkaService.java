package com.fotoalpha.portfolioservice.Service.KafkaService;


import com.fotoalpha.portfolioservice.Entity.Portfolio;
import com.fotoalpha.portfolioservice.KafkaEvents.SavePhotosEvent;
import com.fotoalpha.portfolioservice.Repository.PortfolioServiceDB;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {
    private final PortfolioServiceDB pfDb;

    @Transactional
    public void savePhotos(SavePhotosEvent event) {

        List<String> presignedURLs = event.presignedURLs();
        for (String url : presignedURLs) {
            Portfolio newPortfolio =  Portfolio.builder().photo_url(url).build();
            pfDb.save(newPortfolio);
            log.info("Saved {} to DB", presignedURLs);
        }
    }
}
