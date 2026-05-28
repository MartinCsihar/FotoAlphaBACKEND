package com.fotoalpha.appointmentsservice.KafkaEvents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class AppointmentResponse {
    String type;
    String bunldeSubType;
    String userId;
    String appId;

}
