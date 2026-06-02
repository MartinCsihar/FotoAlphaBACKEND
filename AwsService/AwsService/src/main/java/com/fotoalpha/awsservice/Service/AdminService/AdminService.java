package com.fotoalpha.awsservice.Service.AdminService;

import com.fotoalpha.awsservice.Events.SavePhotosEvent;
import com.fotoalpha.awsservice.Events.SaveProfilePictureEvent;
import com.fotoalpha.awsservice.Kafka.Producer;
import com.fotoalpha.awsservice.RequestResponse.GetPhotosResponse;
import com.fotoalpha.awsservice.RequestResponse.GetVideosResponse;
import com.fotoalpha.awsservice.RequestResponse.UploadFilesReq;
import com.fotoalpha.awsservice.RequestResponse.UploadProfPicReq;
import com.fotoalpha.awsservice.Service.AsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final S3Client s3Client;
    private final AsService asService;
    private final S3Presigner s3Presigner;
    private final Producer producer;
    @Value("${aws.bucket.name}")
    String bucketName;
    @Value("${aws.region}")
    String region;

    public void uploadPhotos(UploadFilesReq uploadFilesReq, String uid, String photoType) throws IOException {
        List<String> urlsForSavePhotosEvent = new ArrayList<>();

        for (MultipartFile file : uploadFilesReq.getFiles()) {
            String prefix = uid + "/PHOTOS/" + "photo_" + UUID.randomUUID().toString().substring(4, 8) ;
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(prefix)
                    .contentType(file.getContentType())
                    .build(), RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String url = photoType.toLowerCase() + "#" + "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + prefix;
            urlsForSavePhotosEvent.add(url);
        }
        SavePhotosEvent event = new SavePhotosEvent(urlsForSavePhotosEvent);
        producer.sendSavePhotosEvent(event);
    }
    public String generatePresignedUrl(String key) throws IOException {
            PutObjectRequest putReq = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType("video/mp4")
                    .build();
            PresignedPutObjectRequest presignedPutReq = s3Presigner.presignPutObject(builder ->  builder
                    .signatureDuration(Duration.ofMinutes(30))
                    .putObjectRequest(putReq));
            return presignedPutReq.url().toString();
    }

    public GetPhotosResponse getPhotosByUID(String uid) {
        String prefix = uid + "/PHOTOS/";
        return  GetPhotosResponse.builder()
                .photoUrls(asService.getPresigendURLs(prefix))
                .build();
    }

    public GetVideosResponse getVideosByUID(String uid) {
        String prefix = uid + "/VIDEOS/";
        return  GetVideosResponse.builder()
                .videoUrls(asService.getPresigendURLs(prefix))
                .build();
    }

    public Object saveProfilePicture(String uid, UploadProfPicReq req) throws IOException {
        MultipartFile profilePicture = req.getProfilePicture();
        String prefix = uid + "/PROFILEPIC/"+"profilepic_"+ UUID.randomUUID().toString().substring(4, 8);
        s3Client.putObject(PutObjectRequest.builder()
                        .key(prefix)
                        .contentType("image/jpeg")
                        .bucket(bucketName)
                .build(), RequestBody.fromInputStream(profilePicture.getInputStream(), profilePicture.getSize()));
        SaveProfilePictureEvent event = SaveProfilePictureEvent.builder()
                .key(prefix)
                .uid(uid)
                .build();
        producer.sendSaveProfilePictureEvent(event);
        return "Profilkép sikeresen feltöltve!";
    }
}
