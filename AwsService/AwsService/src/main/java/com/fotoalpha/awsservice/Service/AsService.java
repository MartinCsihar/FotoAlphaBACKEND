package com.fotoalpha.awsservice.Service;

import com.fotoalpha.awsservice.Events.SavePhotosEvent;
import com.fotoalpha.awsservice.RequestResponse.GetPhotosResponse;
import com.fotoalpha.awsservice.RequestResponse.GetVideosResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class AsService {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.bucket.name}")
    private String bucketName;
    @Value("${aws.region}")
    private String region;


    public GetPhotosResponse getAllPhotos(String uid, String folderName) {
        String prefixPhotos = uid.replace("#", "")+"/PHOTOS/";
        return GetPhotosResponse.builder().subFoldersWithUrls(getNormalUrls(prefixPhotos, folderName)).build();
    }

    public GetVideosResponse getAllVideos(String uid, String folderName) {
        String prefixVideos = uid.replace("#", "")+"/VIDEOS/";
        return GetVideosResponse.builder().videoUrls(getNormalUrls(prefixVideos, folderName)).build();
    }

    public void downloadPhotosZip(String uid, String folderName,  HttpServletResponse response) throws IOException {
        String prefixPhotos = uid.replace("#", "")+"/PHOTOS/"+folderName+"/";
        downloadZip(prefixPhotos, response);
    }

    public void downloadVideosZip(String uid, String folderName, HttpServletResponse response) throws IOException {
        String prefixVideos = uid.replace("#", "")+"/VIDEOS/"+folderName+"/";
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
            ZipEntry entry = new ZipEntry(prefix +"_" + ++i + ".jpg");
            zos.putNextEntry(entry);
            zos.write(reqBytes.asByteArray());
            zos.closeEntry();
        }
        zos.finish();
        zos.close();
    }

//    public String getPresigendURL(String prefix){
//        GetObjectRequest req = GetObjectRequest.builder().bucket(bucketName).key(prefix).build();
//        PresignedGetObjectRequest preReq = s3Presigner.presignGetObject(GetObjectPresignRequest.builder()
//                        .getObjectRequest(req)
//                        .signatureDuration(Duration.ofDays(1))
//                .build());
//        return preReq.url().toString();
//    }

    public List<String> getFolders(String uid, boolean videoFolders) {
        String prefix = "";
        if (!videoFolders) {
            prefix = uid.replace("#", "")+"/PHOTOS/";
        }else{
            prefix = uid.replace("#", "")+"/VIDEOS/";
        }
        return s3Client.listObjectsV2(ListObjectsV2Request.builder().delimiter("/").bucket(bucketName).prefix(prefix).build()).commonPrefixes().stream().map(CommonPrefix::prefix)
                .map(pfx -> pfx.substring(18, pfx.length()-1)).toList();
    }

    public Map<String, Object> getNormalUrls(String prefix, String folderName) {
        Map<String, Object> subFoldersWithUrls = new HashMap<>();
//        List<Map<String, Object>> subFolders = new ArrayList<>();
        ListObjectsV2Response res = s3Client.listObjectsV2(ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .delimiter("/")
                .build());

//        Map<String, Object> subFolder = null;
        for (CommonPrefix sf : res.commonPrefixes()) {
            if (sf.prefix().contains(folderName)) {
                ListObjectsV2Response subContent = s3Client.listObjectsV2(ListObjectsV2Request.builder()
                        .prefix(sf.prefix())
                        .bucket(bucketName)
                        .build());
                List<String> urls = subContent
                        .contents()
                        .stream()
                        .map(s3obj -> "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + s3obj.key())
                        .toList();

                String subPrefix = sf.prefix().substring(18, sf.prefix().length()-1);

//                subFolders.add(subFolder);
                subFoldersWithUrls.put("subFolder", subPrefix);
                subFoldersWithUrls.put("files", urls);
            }
        }
//        subFoldersWithUrls.put("subFolders", subFolders);

        return subFoldersWithUrls;
    }

    public Object getPresigendURLs(String prefix){
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
