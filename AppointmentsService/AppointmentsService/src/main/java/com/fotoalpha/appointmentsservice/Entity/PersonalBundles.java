package com.fotoalpha.appointmentsservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalBundles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rawPhotos;
    private Integer editedPhotos;
    private Integer instantPhotos;
    private Boolean lightPaintPhotos;
    private Integer bundlePrice;

    @ManyToMany
    @JoinTable(
            name = "presonalEvents",
            joinColumns = @JoinColumn(name = "eventId"),
            inverseJoinColumns = @JoinColumn(name="personalBundleId")
    )
    private List<Events> events;
}
