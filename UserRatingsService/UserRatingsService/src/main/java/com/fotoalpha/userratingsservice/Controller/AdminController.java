package com.fotoalpha.userratingsservice.Controller;

import com.fotoalpha.userratingsservice.Service.URSService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin-api")
@RequiredArgsConstructor
public class AdminController {
    private final URSService ursService;

    @GetMapping("/numOfRatings")
    public ResponseEntity<Integer> numOfRatings() {
        return new ResponseEntity<>(ursService.getNumberOfRatings(), HttpStatus.OK);
    }
    @GetMapping("/avgRating")
    public ResponseEntity<String> avgRating() {
        return new ResponseEntity<>(ursService.getAvgRating(), HttpStatus.OK);
    }
}
