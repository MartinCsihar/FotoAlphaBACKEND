package com.fotoalpha.awsservice.Controller;

import com.fotoalpha.awsservice.RequestResponse.*;
import com.fotoalpha.awsservice.Service.AdminService.AdminService;
import com.fotoalpha.awsservice.Service.AsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin-api/")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final AsService asService;

    @PostMapping(value = "/uploadPhotos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadFilesRes> uploadPhotos(@RequestParam("files") List<MultipartFile> files,
                                                      @RequestParam("uid") String uid,
                                                       @RequestParam("photoType") String photoType,
                                                       @RequestParam("folderName") String folderName
    ) {
        try{
            adminService.uploadPhotos(files, folderName,uid, photoType);
            return new ResponseEntity<>(new UploadFilesRes("Sikeres feltöltés!"),  HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(new UploadFilesRes("Nem sikerült feltölteni a fileokat! Hiba: "+e),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "/generatePresignedPutRequests")
    public ResponseEntity<?> uploadVideos(@RequestParam("uid") String uid,
                                               @RequestParam("folderName") String folderName,
                                               @RequestParam("count") Integer count ) {
        try{
            List<String> presignedUrls = new ArrayList<>();
            for(int i = 0; i < count; i++){
                String key = uid.replace("#","").strip()+"/VIDEOS/"+ folderName.strip() + "/" +"video_" + UUID.randomUUID().toString().substring(4,8);
                String url = adminService.generatePresignedUrl(key);
                presignedUrls.add(url);
            }
            return new ResponseEntity<>(presignedUrls,  HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("Nem sikerült feltölteni a videót!",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllPhotos")
    public ResponseEntity<GetPhotosResponse> getPhotosByUID(@RequestParam("uid") String uid,
                                                            @RequestParam("folderName") String folderName

                                                            ) throws IOException {
        return new ResponseEntity<>(adminService.getPhotosByUID(uid, folderName), HttpStatus.OK);
    }
    @GetMapping("/getAllVideos")
    public ResponseEntity<GetVideosResponse> getVideosByUID(@RequestParam("uid") String uid,
                                                            @RequestParam("folderName") String folderName

                                                            ) throws IOException {
        return new ResponseEntity<>(adminService.getVideosByUID(uid, folderName), HttpStatus.OK);
    }

    @PostMapping("/saveProfilePicture")
    public ResponseEntity<?> saveProfilePicture(@RequestParam("uid") String uid,
                                                @ModelAttribute UploadProfPicReq req
                                                ) throws IOException {
        return new ResponseEntity<>(adminService.saveProfilePicture(uid, req), HttpStatus.OK);
    }
    @GetMapping("/getFolderNames")
    public ResponseEntity<?> getFolders(@RequestParam("uid") String uid,
                                        @RequestParam("videoFolders") Boolean videoFolders){

        return new ResponseEntity<>(asService.getFolders(uid, videoFolders), HttpStatus.OK);
    }
}
