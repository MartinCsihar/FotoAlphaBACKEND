package com.fotoalpha.portfolioservice.Controller;

import com.fotoalpha.portfolioservice.Response.GetPairPhotosResponse;
import com.fotoalpha.portfolioservice.Response.GetWeddingPhotosResponse;
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
@RequestMapping("/api/PortfolioService")
public class Controller {
    private final PfService pfService;

    @GetMapping("/getPairPhotos")
    public ResponseEntity<GetPairPhotosResponse> getPairPhotos(@RequestParam("count") int count) {
        List<String> querriedPhotos = pfService.getPairPhotos(count);
        return new  ResponseEntity<>(new GetPairPhotosResponse(querriedPhotos), HttpStatus.OK);
    }

    @GetMapping("/getWeddingPhotos")
    public ResponseEntity<GetWeddingPhotosResponse> getWeddingPhotos(@RequestParam("count") int count) {
        List<String> querriedPhotos = pfService.getWeddingPhotos(count);
        return new ResponseEntity<>(new GetWeddingPhotosResponse(querriedPhotos), HttpStatus.OK);
    }

}
