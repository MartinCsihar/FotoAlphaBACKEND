package com.fotoalpha.userratingsservice.Repo;

import com.fotoalpha.userratingsservice.Entity.URSKeys;
import com.fotoalpha.userratingsservice.Entity.UserRatingsService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface URSRepo extends JpaRepository<UserRatingsService, URSKeys > {
}
