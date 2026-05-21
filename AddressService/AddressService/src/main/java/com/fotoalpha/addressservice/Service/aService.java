package com.fotoalpha.addressservice.Service;

import com.fotoalpha.addressservice.Entity.AddressService;
import com.fotoalpha.addressservice.Events.DeleteAddressEvent;
import com.fotoalpha.addressservice.Events.SaveAddressEvent;
import com.fotoalpha.addressservice.Repo.AddressRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class aService {
    private final AddressRepo aRepo;
    public void saveAddress(SaveAddressEvent event) {
        log.info("Save process started with event: {}", event);
        AddressService newAddress = new AddressService();
        if (event.isPairLocations() == true) {
            newAddress.setId(event.addressId());
            newAddress.setPairLocations(event.pairLocations());
            newAddress.setUserID(event.userID());
            aRepo.save(newAddress);
        }
        newAddress.setId(event.addressId());
        newAddress.setCity(event.city());
        newAddress.setHouseNumber(event.houseNumber());
        newAddress.setStreetType(event.streetType());
        newAddress.setStreetName(event.streetName());
        newAddress.setPostalCode(event.postalCode());
        newAddress.setUserID(event.userID());
        aRepo.save(newAddress);
    }

    public void deleteAddress(DeleteAddressEvent event) {
        aRepo.deleteById(event.addressID());
    }
}
