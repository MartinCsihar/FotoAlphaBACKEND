package com.fotoalpha.addressservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressService {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String postalCode;
    private String streetName;
    private String city;
    private String houseNumber;
    private String streetType;
    private String userID;
}
