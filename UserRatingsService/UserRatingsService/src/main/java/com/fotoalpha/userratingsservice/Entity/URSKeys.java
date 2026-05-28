package com.fotoalpha.userratingsservice.Entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class URSKeys implements Serializable {
    private String userId;
    private String appointmentId;
}
