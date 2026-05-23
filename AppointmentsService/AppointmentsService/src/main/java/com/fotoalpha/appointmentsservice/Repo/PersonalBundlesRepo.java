package com.fotoalpha.appointmentsservice.Repo;

import com.fotoalpha.appointmentsservice.Entity.PersonalBundles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonalBundlesRepo extends JpaRepository<PersonalBundles, Long> {
}
