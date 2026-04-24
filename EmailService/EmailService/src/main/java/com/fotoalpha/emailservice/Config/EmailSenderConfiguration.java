package com.fotoalpha.emailservice.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Slf4j
@Configuration
public class EmailSenderConfiguration {
    @Value("${mail.sender.password}")
    private String mailSenderPassword;
    @Value("${mail.sender.username}")
    private String mailSenderUsername;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        log.info("Mail sender username: {}",mailSenderUsername);
        log.info("Mail sender password: {}",mailSenderPassword);

        mailSender.setUsername(mailSenderUsername);
        mailSender.setPassword(mailSenderPassword.strip());
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        return mailSender;
    }
}
