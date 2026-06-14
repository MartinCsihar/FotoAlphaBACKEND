package com.fotoalpha.userservice.Controller;


import com.fotoalpha.userservice.RequestsResponses.GetUser;
import com.fotoalpha.userservice.RequestsResponses.UserModifyDataRequest;
import com.fotoalpha.userservice.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {
    private final UserService userService;
    @PatchMapping("/modifyPersonalData")
    public ResponseEntity<?> modifyData(@RequestBody UserModifyDataRequest req, Authentication auth){
        String uid = auth.getName().split("\\:")[0];
        return new ResponseEntity<>(userService.modifyUserData(req, uid), HttpStatus.OK);
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
}
