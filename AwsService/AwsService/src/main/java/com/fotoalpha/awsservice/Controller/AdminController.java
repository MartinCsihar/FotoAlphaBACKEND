package com.fotoalpha.awsservice.Controller;

import com.fotoalpha.awsservice.RequestResponse.*;
import com.fotoalpha.awsservice.Service.AdminService.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin-api/")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping(value = "/uploadPhotos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadFilesRes> uploadPhotos(@ModelAttribute UploadFilesReq uploadFilesReq,
                                                      @RequestParam("uid") String uid, @RequestParam("photoType") String photoType) {
        try{
            adminService.uploadPhotos(uploadFilesReq, uid, photoType);
            return new ResponseEntity<>(new UploadFilesRes("Sikeres feltöltés!"),  HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(new UploadFilesRes("Nem sikerült feltölteni a fileokat! Hiba: "+e),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(value = "/generatePresignedPutReq")
    public ResponseEntity<String> uploadVideos(@RequestParam("uid") String uid) {
        try{
            String key = uid+"/VIDEOS/"+"video_" + UUID.randomUUID().toString().substring(4,8);
            return new ResponseEntity<>(adminService.generatePresignedUrl(key),  HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("Nem sikerült feltölteni a videót!",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllPhotos")
    public ResponseEntity<GetPhotosResponse> getPhotosByUID(@RequestParam("uid") String uid) {
        return new ResponseEntity<>(adminService.getPhotosByUID(uid), HttpStatus.OK);
    }
    @GetMapping("/getAllVideos")
    public ResponseEntity<GetVideosResponse> getVideosByUID(@RequestParam("uid") String uid) {
        return new ResponseEntity<>(adminService.getVideosByUID(uid), HttpStatus.OK);
    }

    @PostMapping("/saveProfilePicture")
    public ResponseEntity<?> saveProfilePicture(@RequestParam("uid") String uid, @ModelAttribute UploadProfPicReq req) throws IOException {
        return new ResponseEntity<>(adminService.saveProfilePicture(uid, req), HttpStatus.OK);
    }
}
