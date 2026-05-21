package com.fotoalpha.appointmentsservice.KafkaEvents;

import lombok.Builder;
import lombok.Data;

@Builder
public record SaveAddressEvent(
        String addressId ,Boolean isPairLocations, String pairLocations, String postalCode, String city, String streetName, String streetType, String houseNumber, String userID
) {
}
