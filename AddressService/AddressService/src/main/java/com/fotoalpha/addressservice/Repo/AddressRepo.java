package com.fotoalpha.addressservice.Repo;

import com.fotoalpha.addressservice.Entity.AddressService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressRepo extends JpaRepository<AddressService, UUID> {
    void deleteById(UUID id);
}
