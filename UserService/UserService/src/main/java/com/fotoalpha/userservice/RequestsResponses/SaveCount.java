package com.fotoalpha.userservice.RequestsResponses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SaveCount {
    int photoCount;
    int  videoCount;
    String email;
}
