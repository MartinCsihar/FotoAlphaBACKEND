package com.fotoalpha.addressservice.Repo;

import com.fotoalpha.addressservice.Entity.AddressService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressRepo extends JpaRepository<AddressService, UUID> {
    void deleteById(String id);

    @Query(value = "select * from address_service where id = ?1", nativeQuery = true)
    AddressService getAddress(String id);
}
