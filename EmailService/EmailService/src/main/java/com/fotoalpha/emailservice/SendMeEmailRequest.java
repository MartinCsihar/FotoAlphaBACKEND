package com.fotoalpha.emailservice.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendMeEmailRequest {
    String userEmail;
    String subject;
    String message;
}
