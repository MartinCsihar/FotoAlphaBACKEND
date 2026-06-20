package com.fotoalpha.addressservice.Events;

import lombok.Builder;

import java.util.List;

@Builder
public record GetAddressesReqEvent(
        String correlationId,
    List<String> addressIds
) {
}
