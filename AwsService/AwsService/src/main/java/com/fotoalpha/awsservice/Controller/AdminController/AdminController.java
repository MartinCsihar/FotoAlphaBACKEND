package com.fotoalpha.awsservice.Controller.AdminController;

import com.fotoalpha.awsservice.Response.GetPhotosResponse;
import com.fotoalpha.awsservice.Response.GetVideosResponse;
import com.fotoalpha.awsservice.Response.UploadFilesReq;
import com.fotoalpha.awsservice.Response.UploadFilesRes;
import com.fotoalpha.awsservice.Service.AdminService.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin-api/")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping(value = "/uploadPhotos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadFilesRes> uploadPhotos(@ModelAttribute UploadFilesReq uploadFilesReq,
                                                      @RequestParam("uid") String uid) {
        try{
            adminService.uploadPhotos(uploadFilesReq, uid);
            return new ResponseEntity<>(new UploadFilesRes("Sikeres feltöltés!"),  HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(new UploadFilesRes("Nem sikerült feltölteni a fileokat! Hiba: "+e),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(value = "/uploadVideos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadFilesRes> uploadVideos(@ModelAttribute UploadFilesReq uploadFilesReq,
                                                       @RequestParam("uid") String uid) {
        try{
            adminService.uploadVideos(uploadFilesReq, uid);
            return new ResponseEntity<>(new UploadFilesRes("Sikeres feltöltés!"),  HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(new UploadFilesRes("Nem sikerült feltölteni a fileokat! Hiba:  "+e),HttpStatus.BAD_REQUEST);
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
