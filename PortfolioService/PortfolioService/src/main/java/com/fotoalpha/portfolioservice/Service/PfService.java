package com.fotoalpha.portfolioservice.Service;

import com.fotoalpha.portfolioservice.Repository.PortfolioServiceDB;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PfService {
    private final PortfolioServiceDB pfDb;
    public List<String> getPortfolioPhotos(int count, String type) {
        return pfDb.getPortfolioPhotos(count, type);
    }
}
