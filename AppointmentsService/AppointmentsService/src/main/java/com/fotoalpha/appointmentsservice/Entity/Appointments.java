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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Appointments {
    @Id
    private String id;
    private String addressId;
    private LocalDate orderDate;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String userId;

    @Enumerated(EnumType.STRING)
    private AppointmentType type;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "appointment")
    private List<Bundles> bundleId =  new ArrayList<>();

    @OneToMany(mappedBy = "appointment")
    private List<PersonalBundles> presonalBundleId = new  ArrayList<>();

    @PrePersist
    public void prePersist() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(chars.length());
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
            id = "#" + sb.toString();
        }
    }
}
