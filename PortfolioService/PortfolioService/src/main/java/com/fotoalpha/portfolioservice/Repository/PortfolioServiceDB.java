package com.fotoalpha.portfolioservice.Repository;

import com.fotoalpha.portfolioservice.Entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PortfolioServiceDB extends JpaRepository<Portfolio, UUID> {
    @Query(value = "SELECT PhotoURL FROM Portfolio WHERE PhotoType = 'Pair' LIMIT ?1;", nativeQuery = true)
    List<String> getPairPhotos(int count);

    @Query(value = "SELECT PhotoURL FROM Portfolio WHERE PhotoType = 'Wedding' LIMIT ?1", nativeQuery = true)
    List<String> getWeddingPhotos(int count);

}
