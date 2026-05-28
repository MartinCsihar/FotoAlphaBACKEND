package com.fotoalpha.userratingsservice.Repo;

import com.fotoalpha.userratingsservice.Entity.URSKeys;
import com.fotoalpha.userratingsservice.Entity.UserRatingsService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface URSRepo extends JpaRepository<UserRatingsService, URSKeys > {
    @Query(value = "select appointment_id from user_ratings_service", nativeQuery = true)
    List<String> getAllAppointmentIds();
}
