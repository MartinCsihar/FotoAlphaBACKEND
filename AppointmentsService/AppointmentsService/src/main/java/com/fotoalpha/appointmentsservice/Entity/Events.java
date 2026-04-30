package com.fotoalpha.appointmentsservice.Entity;

import com.fotoalpha.appointmentsservice.Enums.EventName;
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
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;
    private Integer eventPrice;
    @Enumerated(EnumType.STRING)
    private EventName eventName;
    @ManyToMany
    private List<PersonalBundles> personalBundles;

}
