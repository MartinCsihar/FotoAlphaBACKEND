package com.fotoalpha.awsservice.Controller.AdminController;

import com.fotoalpha.awsservice.Response.GetPhotosResponse;
import com.fotoalpha.awsservice.Response.GetVideosResponse;
import com.fotoalpha.awsservice.Response.UploadFilesReq;
import com.fotoalpha.awsservice.Response.UploadFilesRes;
import com.fotoalpha.awsservice.Service.AdminService.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/AwsService")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/upload")
    public ResponseEntity<UploadFilesRes> uploadFiles(@RequestParam("file") UploadFilesReq uploadFilesReq,
                                                      @RequestParam("uid") String uid) {
        try{
            adminService.uploadFiles(uploadFilesReq, uid);
            return new ResponseEntity<>(new UploadFilesRes("Sikeres feltöltés!"),  HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(new UploadFilesRes("Nem sikerült feltölteni a fileokat!"),HttpStatus.BAD_REQUEST);
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
}
