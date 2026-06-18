package com.fotoalpha.userservice.Controller;


import com.fotoalpha.userservice.RequestsResponses.GetUser;
import com.fotoalpha.userservice.RequestsResponses.UserModifyDataRequest;
import com.fotoalpha.userservice.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {
    private final UserService userService;
    @PatchMapping("/modifyPersonalData")
    public ResponseEntity<?> modifyData(@RequestBody UserModifyDataRequest req, Authentication auth) throws Exception {
        String uid = auth.getName().split(":")[0];
        try{
            return new ResponseEntity<>(userService.modifyUserData(req, uid), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getUserData")
    public ResponseEntity<GetUser> getUserData(Authentication auth){
        String uid = auth.getName().split("\\:")[0];
        try{
            return new ResponseEntity<>(userService.getUserData(uid), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "Sikeres kijelentkezés!";
    }

    @GetMapping("/numberOfPhotosAndVideos")
    public ResponseEntity<Map<String, Integer>> getNumberOfPhotos(Authentication auth){
        String uid = auth.getName().split(":")[0];
        return new ResponseEntity<>(userService.getNumberOfPhotosMadeForUser(uid), HttpStatus.OK);
    }
}
