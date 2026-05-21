package com.fotoalpha.appointmentsservice.Repo;

import com.fotoalpha.appointmentsservice.Entity.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppRepo extends JpaRepository<Appointments, String> {
    List<Appointments> findAllByUserId(String userId);
    Appointments findByIdAndUserId(String appId, String userId);

    @Modifying
    @Query(value = "UPDATE Appointments SET status = 'cancelled' WHERE  id = ?1 AND user_id = ?2", nativeQuery = true)
    int cancelAppointment(String appid, String userid);
}
