package com.fotoalpha.appointmentsservice.ResponseRequest;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class GetAllAppointment {
    String id;
    String bundleType;
    String bundleName;
    LocalDate orderDate;
    LocalDate eventDate;
    LocalTime eventTime;
    Integer price;
    String location;
    String status;
    Boolean isRated;

}
