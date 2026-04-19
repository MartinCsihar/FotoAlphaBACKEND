package com.fotoalpha.portfolioservice.Service;

import com.fotoalpha.portfolioservice.Repository.PortfolioServiceDB;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PfService {
    List<String> porfolioPairPhotos;

    private final PortfolioServiceDB pfDb;

    public List<String> getPairPhotos(int count) {
        return pfDb.getPairPhotos(count);
    }

    public List<String> getWeddingPhotos(int count) {
        return pfDb.getWeddingPhotos(count);
    }
}
