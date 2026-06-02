package com.fotoalpha.userratingsservice.Controller;

import com.fotoalpha.userratingsservice.RequestsResponses.FetchAllRatingsResponse;
import com.fotoalpha.userratingsservice.RequestsResponses.SaveRatingRequest;
import com.fotoalpha.userratingsservice.Service.URSService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {
    private final URSService ursService;

    @PostMapping("/save")
    public ResponseEntity<Boolean> saveRating(@RequestBody() SaveRatingRequest req,
                                              Authentication authentication,
                                              @RequestParam("appid") String appid){
        String uid = authentication.getName().split("\\:")[0];
        return new ResponseEntity<>(ursService.saveRating(req, uid, appid),  HttpStatus.CREATED);
    }
}
