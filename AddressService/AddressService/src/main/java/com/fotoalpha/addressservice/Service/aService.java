package com.fotoalpha.addressservice.Service;

import com.fotoalpha.addressservice.Entity.AddressService;
import com.fotoalpha.addressservice.Events.DeleteAddressEvent;
import com.fotoalpha.addressservice.Events.GetAddressesReqEvent;
import com.fotoalpha.addressservice.Events.GetAddressesResEvent;
import com.fotoalpha.addressservice.Events.SaveAddressEvent;
import com.fotoalpha.addressservice.Kafka.Producer;
import com.fotoalpha.addressservice.Repo.AddressRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class aService {
    private final AddressRepo aRepo;
    private final Producer producer;
    public void getLocations(GetAddressesReqEvent req) {
        List<String> ids = req.addressIds();
        List<String> locations = ids.stream().map(this::getLocationOfAppointment).toList();
        GetAddressesResEvent res = GetAddressesResEvent.builder()
                .correlationId(req.correlationId())
                .addresses(locations)
                .build();
        producer.sendGetAddressResEvent(res);
    }


    private  String getLocationOfAppointment(String addressId) {
         AddressService address = aRepo.getAddress(addressId);
         if(address.getPairLocations() == null) {
             String district = address.getPostalCode().substring(1, 3);
             return "" + address.getPostalCode()  + " " + address.getCity() + ", " + district  + ". kerület, " + address.getStreetName() + " " + address.getStreetType() + " " + address.getHouseNumber().replace(".","") +".";

         }
         return address.getPairLocations();

    }


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
