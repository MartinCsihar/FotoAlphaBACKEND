package com.fotoalpha.userservice.Controller;

import com.fotoalpha.userservice.RequestsResponses.GetAllUsersResponse;
import com.fotoalpha.userservice.RequestsResponses.SaveCount;
import com.fotoalpha.userservice.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin-api")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping("/numberOfUsers")
    public Integer numberOfUsers() {
        return userService.numberOfUsers();
    }

    @GetMapping("/numberOfPhotosVideos")
    public Map<String, Integer> numberOfPhotos() {
        return userService.numberOfPhotosVideos();
    }
    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
    @GetMapping("/getUser")
    public ResponseEntity<?> fetchUsers(@RequestParam("uid") String uid) {
        return new ResponseEntity<>(userService.getUser(uid), HttpStatus.OK);
    }
    @PostMapping("/saveTheNumberOfPhotosAndVideos")
    public ResponseEntity<?> saveTheNumberOfPhotosVideos(@RequestBody SaveCount req) {
        return new ResponseEntity<>(userService.saveNumOfPhotosAndOrVideosForUser(req), HttpStatus.OK);
    }

    @GetMapping("/auth")
    public String getAuth(Authentication authentication) {
        return authentication.getName();
    }

}
