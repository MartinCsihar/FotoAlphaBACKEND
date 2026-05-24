package com.fotoalpha.appointmentsservice.Controller;


import com.fotoalpha.appointmentsservice.Entity.Appointments;
import com.fotoalpha.appointmentsservice.Entity.User;
import com.fotoalpha.appointmentsservice.ResponseRequest.AdminModifyDetailsRequest;
import com.fotoalpha.appointmentsservice.Service.appService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/admin-api")
@RequiredArgsConstructor
public class AdminController {
    private final appService appService;

    @GetMapping("/totalIncome")
    public ResponseEntity<Map<String, Integer>> getTotalIncome(){
        return new ResponseEntity<>(appService.getTotalIncome(), HttpStatus.OK);
    }
    @GetMapping("/countAppointments")
    public ResponseEntity<Map<String, Integer>> getCountAppointments(){
        return new ResponseEntity<>(appService.countAppointments(), HttpStatus.OK);
    }
    @PatchMapping("/modify")
    public ResponseEntity<Boolean> modifyAppointment(@RequestBody AdminModifyDetailsRequest req ,
                                                     @RequestParam("appid") String appId,
                                                     @RequestParam("uid") String uid) {
        return new ResponseEntity<>(appService.modifyAppDetails(appId, uid, req), HttpStatus.OK);
    }
    @PutMapping("/accomplish")
    public ResponseEntity<Boolean> accomplishAppointment(@RequestParam("uid") String uid, @RequestParam("appid") String appid) {
        return new ResponseEntity<>(appService.accomplishAppointment(appid, uid), HttpStatus.OK);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteAppointment(@RequestParam("appid") String appid) {
        return new ResponseEntity<>(appService.deleteAppointment(appid), HttpStatus.OK);
    }
    @GetMapping("/allAppointmentsOfUser")
    public ResponseEntity<List<Appointments>> getAllAppointmentsOfUser(@RequestParam("uid") String uid) {
        return new ResponseEntity<>(appService.getAllAppointmentsByUserId(uid), HttpStatus.OK);
    }
    @GetMapping("/allAppointmentsOfUserByAppId")
    public ResponseEntity<Appointments> getAllAppointmentsOfUserByAppId(@RequestParam("uid") String uid, @RequestParam("appid") String appid) {
        return new ResponseEntity<>(appService.getAllAppointmentsOfUserByAppId(appid, uid), HttpStatus.OK);
    }
    @GetMapping("/allAppointments")
    public ResponseEntity<List<Appointments>> getAllAppointments() {
        return new ResponseEntity<>(appService.allAppointments(), HttpStatus.OK);
    }
    @GetMapping("/countClients")
    public ResponseEntity<Integer> countClients() {
        return new ResponseEntity<>(appService.countClients(), HttpStatus.OK);
    }
    @GetMapping("/fetchUsers")
    public ResponseEntity<List<User>> getAllUsers() throws ExecutionException, InterruptedException, TimeoutException {
        try{
            return new ResponseEntity<>(appService.fetchUsers(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
