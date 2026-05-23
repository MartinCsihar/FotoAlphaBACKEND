package com.fotoalpha.appointmentsservice.Controller;


import com.fotoalpha.appointmentsservice.Service.appService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin-api")
@RequiredArgsConstructor
public class AdminController {
    private final appService appService;

    @GetMapping("/totalIncome")
    public ResponseEntity<Map<String, Integer>> getTotalIncome(){
        return new ResponseEntity<>(appService.getTotalIncome(), HttpStatus.OK);
    }
}
