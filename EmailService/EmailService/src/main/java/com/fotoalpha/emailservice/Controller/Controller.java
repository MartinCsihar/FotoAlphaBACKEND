package com.fotoalpha.emailservice.Controller;

import com.fotoalpha.emailservice.SendMeEmailRequest;
import com.fotoalpha.emailservice.Service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class Controller {
    private final EmailService emailService;
    @PostMapping(value = "/api/sendMail")
    public ResponseEntity<String> sendMail(@RequestBody SendMeEmailRequest sendMeEmailRequest) throws MessagingException {
        try {
            return new ResponseEntity<>(emailService.sendMeMail(sendMeEmailRequest), HttpStatus.OK);
        }catch (MessagingException me){
            log.error(me.getMessage());
            return new ResponseEntity<>("Sajnos nem sikerül elküldeni az emailt! :(", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
