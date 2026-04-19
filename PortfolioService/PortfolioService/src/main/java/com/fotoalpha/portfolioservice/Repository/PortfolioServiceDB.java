package com.fotoalpha.portfolioservice.Repository;

import com.fotoalpha.portfolioservice.Entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PortfolioServiceDB extends JpaRepository<Portfolio, UUID> {

