package com.fotoalpha.appointmentsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AppointmentsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppointmentsServiceApplication.class, args);
    }

}
