package com.fotoalpha.awsservice.Controller;

import com.fotoalpha.awsservice.RequestResponse.GetPhotosResponse;
import com.fotoalpha.awsservice.RequestResponse.GetVideosResponse;
import com.fotoalpha.awsservice.Service.AsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class Controller {
    private final AsService asService;

    @GetMapping("/auth")
    public String getAuth(Authentication authentication){
        return authentication.getName() + " " + authentication.getAuthorities().toString();
    }

    @GetMapping("/getAllPhotos")
    public ResponseEntity<GetPhotosResponse> getAllPhotos(Authentication authentication,
                                                          @RequestParam("folderName") String folderName
                                                         ) {
       String uid = authentication.getName().split(":")[0];
        return new ResponseEntity<>(asService.getAllPhotos(uid, folderName), HttpStatus.OK);
    }
    @GetMapping("/getFolderNames")
    public ResponseEntity<?> getFolders(Authentication authentication,
                                        @RequestParam("videoFolders") Boolean videoFolders){
        String uid = authentication.getName().split(":")[0];
        return new ResponseEntity<>(asService.getFolders(uid, videoFolders), HttpStatus.OK);
    }

    @GetMapping("/getAllVideos")
    public ResponseEntity<GetVideosResponse> getAllVideos(Authentication authentication,
                                                          @RequestParam("folderName") String folderName
                                                         ) {
       String uid = authentication.getName().split(":")[0];
        return new ResponseEntity<>(asService.getAllVideos(uid, folderName), HttpStatus.OK);
    }

    @GetMapping("/downloadAllPhotos")
    public void downloadPhotos(HttpServletResponse response,
                               @RequestParam("folderName") String folderName) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String uid = authentication.getName().split(":")[0];
        asService.downloadPhotosZip(uid, folderName,response );
    }

    @GetMapping("/downloadAllVideos")
    public void downloadVideos(HttpServletResponse response, @RequestParam("folderName") String folderName) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String uid = authentication.getName().split(":")[0];
        asService.downloadVideosZip(uid,folderName ,response);
    }

}