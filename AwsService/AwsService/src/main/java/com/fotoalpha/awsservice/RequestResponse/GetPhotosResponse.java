package com.fotoalpha.awsservice.RequestResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class GetPhotosResponse {
    Map<String, Object> subFoldersWithUrls;


}
