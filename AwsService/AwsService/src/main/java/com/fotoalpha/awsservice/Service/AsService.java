package com.fotoalpha.awsservice.Service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class AsService {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.bucket.name}")
    private String bucketName;

    public List<String> getAllPhotos(String username) {
        String prefixPhotos = username+"/PHOTOS/";
        return getPresigendURLs(prefixPhotos);
    }

    public List<String> getAllVideos(String username) {
        String prefixVideos = username+"/VIDEOS/";
        return getPresigendURLs(prefixVideos);
    }

    public void downloadPhotosZip(String username, HttpServletResponse response) throws IOException {
        String prefixPhotos = username+"/PHOTOS/";
        downloadZip(prefixPhotos, response);
    }

    public void downloadVideosZip(String username, HttpServletResponse response) throws IOException {
        String prefixVideos = username+"/VIDEOS/";
        downloadZip(prefixVideos, response);
    }

    private void downloadZip(String prefix, HttpServletResponse response ) throws IOException {
        List<String> keys = getKeys(prefix);
        int i = 0;
        ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
        for (String key : keys) {
            ResponseBytes<GetObjectResponse> reqBytes = s3Client.getObjectAsBytes(GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());
            ZipEntry entry = new ZipEntry(prefix + ++i);
            zos.putNextEntry(entry);
            zos.write(reqBytes.asByteArray());
            zos.closeEntry();
        }
        zos.finish();
        zos.close();
    }

    public List<String> getPresigendURLs(String prefix){
        List<String>  presignedURLs = new ArrayList<>();
        ListObjectsV2Response res = s3Client.listObjectsV2(ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build());
        List<String> keys = res.contents().stream().map(S3Object::key).toList();
        for (String key : keys) {
            GetObjectRequest req = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            PresignedGetObjectRequest preReq = s3Presigner.presignGetObject(GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofDays(1))
                    .getObjectRequest(req)
                    .build()
            );
            presignedURLs.add(preReq.url().toString());
        }
        return presignedURLs;
    }
    private List<String> getKeys(String prefix){
        ListObjectsV2Response res = s3Client.listObjectsV2(ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build());
        return res.contents().stream().map(S3Object::key).toList();
    }

}
