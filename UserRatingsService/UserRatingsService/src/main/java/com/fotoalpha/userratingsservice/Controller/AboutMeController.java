package com.fotoalpha.userratingsservice.Controller;

import com.fotoalpha.userratingsservice.RequestsResponses.FetchAllRatingsResponse;
import com.fotoalpha.userratingsservice.Service.URSService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/about")
@RequiredArgsConstructor
public class AboutMeController {
    private final URSService ursService;
    @GetMapping("/getAllRatings")
    public ResponseEntity<FetchAllRatingsResponse> getRatings() throws ExecutionException, InterruptedException, TimeoutException {
        try {
            return new ResponseEntity<>(ursService.getRatings(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
