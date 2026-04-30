package com.fotoalpha.appointmentsservice.ResponseRequest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StripeApiUrlResponse {
    String transferURL;
}
