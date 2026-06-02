package com.fotoalpha.awsservice.RequestResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class UploadProfPicReq {
    MultipartFile profilePicture;
}
