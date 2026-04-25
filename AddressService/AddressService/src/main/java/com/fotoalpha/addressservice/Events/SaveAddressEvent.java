package com.fotoalpha.addressservice.Events;

import java.util.UUID;

public record SaveAddressEvent(
        String postalCode, String city, String streetName, String streetType, String houseNumber, String userID
) {
}
