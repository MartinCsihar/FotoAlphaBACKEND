package com.fotoalpha.appointmentsservice.Entity;


import com.fotoalpha.appointmentsservice.Enums.AppointmentType;
import com.fotoalpha.appointmentsservice.Enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Appointments {
    // Generated
    @Id
    private String id;
    private String addressId;
    private LocalDate orderDate;
    //
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String userId;

    @Enumerated(EnumType.STRING)
    private AppointmentType type;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "bundle_id")
    private Bundles bundle;

    @ManyToOne
    @JoinColumn(name = "personal_bundle_id")
    private PersonalBundles presonalBundle;

    @PrePersist
    public void prePersist() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb_appId = new StringBuilder(chars.length());
        StringBuilder sb_addressId = new StringBuilder(chars.length());
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) {
            sb_appId.append(chars.charAt(rnd.nextInt(chars.length())));
            sb_addressId.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        id = "#" + sb_appId.toString();
        addressId = sb_addressId.toString();
        orderDate = LocalDate.now();
        status = Status.pending;
    }
}
