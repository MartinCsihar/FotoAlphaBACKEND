package com.fotoalpha.portfolioservice.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetWeddingPhotosResponse {
    List<String> portfolioWeddingPhotos;
}
