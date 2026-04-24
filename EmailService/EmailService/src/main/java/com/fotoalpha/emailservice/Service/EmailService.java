package com.fotoalpha.emailservice.Service;

import com.fotoalpha.emailservice.Objects.SendMeEmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public String sendMeMail(SendMeEmailRequest sendMeEmailRequest) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setTo(sendMeEmailRequest.getUserEmail());
        mimeMessageHelper.setText( sendMeEmailRequest.getSubject() + "\n" + sendMeEmailRequest.getMessage()+ "\n" + sendMeEmailRequest.getUserEmail(), false);
        mimeMessageHelper.setSubject("Egy felhasználó üzenetet küldött!");

        mailSender.send(mimeMessage);
        return "Email sikeresen elküldve!";

    }
}
