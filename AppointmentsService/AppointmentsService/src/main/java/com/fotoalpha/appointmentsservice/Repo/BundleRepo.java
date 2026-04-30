package com.fotoalpha.appointmentsservice.Repo;

import com.fotoalpha.appointmentsservice.Entity.Bundles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BundleRepo extends JpaRepository<Bundles, Long> {
}
