package com.fotoalpha.userratingsservice.RequestsResponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {
    String type;
    String bunldeSubType;
    String userId;
    String appId;
}
