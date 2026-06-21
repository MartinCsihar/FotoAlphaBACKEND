package com.fotoalpha.appointmentsservice.Stripe;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequest {
    private Long amount;
    private String appId;
    private String userId;
    private String userEmail;
}

