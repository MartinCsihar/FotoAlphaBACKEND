package com.fotoalpha.addressservice.Events;

import java.util.UUID;

public record DeleteAddressEvent(
        UUID addressID
) {
}
