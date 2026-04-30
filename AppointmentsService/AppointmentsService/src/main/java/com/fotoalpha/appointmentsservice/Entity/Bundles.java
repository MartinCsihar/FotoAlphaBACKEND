package com.fotoalpha.appointmentsservice.Entity;

import com.fotoalpha.appointmentsservice.Enums.BundleSubType;
import com.fotoalpha.appointmentsservice.Enums.BundleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bundles {
    @Id
    private Long bundleId;
    @Enumerated(EnumType.STRING)
    private BundleType bundleType;
    @Enumerated(EnumType.STRING)
    private BundleSubType bundleSubType;
    private Integer bundlePrice;
    private String pairLocations;


}
