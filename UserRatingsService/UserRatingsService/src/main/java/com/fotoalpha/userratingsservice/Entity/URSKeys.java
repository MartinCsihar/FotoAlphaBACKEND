package com.fotoalpha.userratingsservice.Entity;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Data
public class URSKeys implements Serializable {
    private String userId;
    private String appointmentId;
}
