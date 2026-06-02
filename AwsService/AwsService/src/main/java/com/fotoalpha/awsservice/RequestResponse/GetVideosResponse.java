package com.fotoalpha.awsservice.RequestResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class GetVideosResponse {
    List<String> presignedVideoUrls;
}
