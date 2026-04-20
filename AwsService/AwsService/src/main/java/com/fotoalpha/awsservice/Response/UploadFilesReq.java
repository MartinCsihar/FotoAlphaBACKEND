package com.fotoalpha.awsservice.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class UploadFilesReq {
    List<MultipartFile> files;
}
