package com.fotoalpha.appointmentsservice.Controller;

import com.fotoalpha.appointmentsservice.Entity.Appointments;
import com.fotoalpha.appointmentsservice.Entity.User;
import com.fotoalpha.appointmentsservice.Enums.AppointmentType;
import com.fotoalpha.appointmentsservice.Enums.Status;
import com.fotoalpha.appointmentsservice.Kafka.Consumer;
import com.fotoalpha.appointmentsservice.Repo.AppRepo;
import com.fotoalpha.appointmentsservice.ResponseRequest.*;
import com.fotoalpha.appointmentsservice.Service.appService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final appService appService;
    private final AppRepo repo;
    private final Consumer consumer;

//    @PostMapping("/save")
//    public String saveApp(@RequestBody SaveWeddingAppointmentRequest req, @RequestParam("uid")String uid){
//        try {
//            Appointments appointment = Appointments.builder()
//                    .appointmentTime(req.getAppointmentTime())
//                    .appointmentDate(req.getAppointmentDate())
//                    .type((AppointmentType) req.getType())
//                    .bundle(req.getBundle())
//                    .presonalBundle(req.getPersonalBundle())
//                    .userId(uid)
//                    .build();
//            repo.save(appointment);
//            return "OK";
//        }
//        catch (Exception e){
//            return e.getMessage();
//        }
//    }

    @GetMapping("/appointment")
    public ResponseEntity<GetAppointmentByIdResponse> getAppointmentById(@RequestParam("appId") String appId, @RequestParam("uid") String uid) {
        return new ResponseEntity<>(appService.getAppointmentByAppointmentIdAndUserId(appId, uid), HttpStatus.OK);
    }

    @PutMapping("/cancelAppointment")
    public ResponseEntity<Boolean> cancelAppointment(@RequestParam("appid") String appointmentId, @RequestParam("uid") String uid) {
        return new ResponseEntity<>(appService.cancelAppointment(uid, appointmentId), HttpStatus.OK);
    }
    @PostMapping("/savePairAppointment")
    public ResponseEntity<Boolean> savePairAppointment(@RequestBody SavePairAppointmentRequest req, @RequestParam("uid") String uid) throws ExecutionException, InterruptedException, TimeoutException {
        try{
            return new ResponseEntity<>(appService.savePairAppointment(req, uid), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/saveWeddingAppointment")
    public ResponseEntity<Boolean> saveWeddingAppointment(@RequestBody SaveWeddingAppointmentRequest req, @RequestParam("uid") String uid) throws ExecutionException, InterruptedException, TimeoutException {
        try {
            return new ResponseEntity<>(appService.saveWeddingAppointment(req, uid), HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/saveOwnMadeWeddingAppointment")
    public ResponseEntity<Boolean> saveOwnMadeWeddingAppointment(@RequestBody SaveOwnMadeWeddingAppointmentRequest req, @RequestParam String uid) throws ExecutionException, InterruptedException, TimeoutException {
        return new ResponseEntity<>(appService.saveOwnMadeApp(req, uid), HttpStatus.OK);
    }


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
