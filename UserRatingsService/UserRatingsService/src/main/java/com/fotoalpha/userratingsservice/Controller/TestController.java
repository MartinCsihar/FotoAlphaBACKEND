package com.fotoalpha.userratingsservice.Controller;

import com.fotoalpha.userratingsservice.RequestsResponses.SaveRatingRequest;
import com.fotoalpha.userratingsservice.Service.URSService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final URSService ursService;
    @GetMapping("/numOfRatings")
    public ResponseEntity<Integer> numOfRatings() {
        return new ResponseEntity<>(ursService.getNumberOfRatings(), HttpStatus.OK);
    }

    @GetMapping("/avgRating")
    public ResponseEntity<String> avgRating() {
        return new ResponseEntity<>(ursService.getAvgRating(), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Boolean> saveRating(@RequestBody() SaveRatingRequest req,
                                              @RequestParam("uid") String uid,
                                              @RequestParam("appid") String appid){
        return new ResponseEntity<>(ursService.saveRating(req, uid, appid),  HttpStatus.CREATED);
    }
}
