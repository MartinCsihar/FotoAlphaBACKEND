package com.fotoalpha.awsservice.Response;

import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.List;

@Data
@AllArgsConstructor
public class GetPhotosResponse {
    List<String> presignedPhotoUrls;


}
