package com.fotoalpha.awsservice.Service.AdminService;

import com.fotoalpha.awsservice.Events.SavePhotosEvent;
import com.fotoalpha.awsservice.Kafka.Producer;
import com.fotoalpha.awsservice.Response.GetPhotosResponse;
import com.fotoalpha.awsservice.Response.GetVideosResponse;
import com.fotoalpha.awsservice.Response.UploadFilesReq;
import com.fotoalpha.awsservice.Service.AsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final S3Client s3Client;
    private final AsService asService;
    private final Producer producer;

    @Value("${aws.bucket.name}")
    String bucketName;

    public void uploadFiles(UploadFilesReq uploadFilesReq, String uid) throws IOException {

        List<String> presignedUrlsForSavePhotosEvent = new ArrayList<>();
        for (MultipartFile file : uploadFilesReq.getFiles()) {
            String prefix = uid + "/" + file.getContentType().toUpperCase() + "/" ;
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(prefix)
                    .contentType(file.getContentType())
                    .build(), RequestBody.fromBytes(file.getBytes()));

            presignedUrlsForSavePhotosEvent.add(asService.getPresigendURL(prefix));
        }
        SavePhotosEvent event = new SavePhotosEvent(presignedUrlsForSavePhotosEvent);
        producer.sendSavePhotosEvent(event);
    }

    public GetPhotosResponse getPhotosByUID(String uid) {
        String prefix = uid + "/PHOTOS/";
        return  GetPhotosResponse.builder()
                .presignedPhotoUrls(asService.getPresigendURLs(prefix))
                .build();
    }

    public GetVideosResponse getVideosByUID(String uid) {
        String prefix = uid + "/VIDEOS/";
        return  GetVideosResponse.builder()
                .presignedVideoUrls(asService.getPresigendURLs(prefix))
                .build();
    }
}
