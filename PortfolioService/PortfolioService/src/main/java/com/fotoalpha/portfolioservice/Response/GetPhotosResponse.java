package com.fotoalpha.portfolioservice.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@AllArgsConstructor
public class GetPhotosResponse {
    List<String> portfolioPhotos;
}
