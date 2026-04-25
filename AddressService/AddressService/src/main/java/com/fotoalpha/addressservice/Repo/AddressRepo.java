package com.fotoalpha.addressservice.Repo;

import com.fotoalpha.addressservice.Entity.AddressService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepo extends JpaRepository<AddressService, UUID> {
}
