package com.fotoalpha.appointmentsservice.Entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {
    String city;
    String houseNumber;
    String pairLocations;
    String postalCode;
    String streetName;
    String streetType;
    String userId;
}
