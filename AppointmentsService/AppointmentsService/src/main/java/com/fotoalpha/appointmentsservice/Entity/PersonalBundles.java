package com.fotoalpha.appointmentsservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Random;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalBundles {
    @Id
    private String id;
    private Integer rawPhotos;
    private Integer editedPhotos;
    private Integer instantPhotos;
    private Boolean lightPaintPhotos;
    private Integer bundlePrice;

    @ManyToMany
    @JoinTable(
            name = "personalEvents",
            joinColumns = @JoinColumn(name = "personal_bundle_id"),
            inverseJoinColumns = @JoinColumn(name="event_id")
    )
    private List<Events> events;
    @PrePersist
    public void prePersist() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb_appId = new StringBuilder(7);
        Random rnd = new Random();
        for (int i = 0; i<8; i++) sb_appId.append(chars.charAt(rnd.nextInt(chars.length())));
        id = sb_appId.toString();
    }
}
