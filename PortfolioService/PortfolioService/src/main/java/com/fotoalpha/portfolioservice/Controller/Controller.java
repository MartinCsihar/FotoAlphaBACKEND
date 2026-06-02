package com.fotoalpha.portfolioservice.Controller;

import com.fotoalpha.portfolioservice.Response.GetPhotosResponse;
import com.fotoalpha.portfolioservice.Service.PfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class Controller {
    private final PfService pfService;

    @GetMapping("/getPortfolioPhotos")
    public ResponseEntity<?> getPortfolioPhotos(@RequestParam("count") int count,
                                                               @RequestParam("type") String type) {
        List<String> querriedPhotos = pfService.getPortfolioPhotos(count, type);
        return new  ResponseEntity<>(new GetPhotosResponse(querriedPhotos), HttpStatus.OK);
    }


}
