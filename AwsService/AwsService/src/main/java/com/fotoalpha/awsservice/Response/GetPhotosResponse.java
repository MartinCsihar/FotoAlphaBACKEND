package com.fotoalpha.awsservice.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class GetPhotosResponse {
    List<String> presignedPhotoUrls;


}
