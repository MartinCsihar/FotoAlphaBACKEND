package com.fotoalpha.appointmentsservice.Repo;

import com.fotoalpha.appointmentsservice.Entity.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppRepo extends JpaRepository<Appointments, String> {
    List<Appointments> findAllByUserId(String userId);
}
