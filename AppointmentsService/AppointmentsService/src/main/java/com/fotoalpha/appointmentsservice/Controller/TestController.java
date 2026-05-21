package com.fotoalpha.appointmentsservice.Controller;

import com.fotoalpha.appointmentsservice.Entity.Appointments;
import com.fotoalpha.appointmentsservice.Enums.AppointmentType;
import com.fotoalpha.appointmentsservice.Enums.Status;
import com.fotoalpha.appointmentsservice.Repo.AppRepo;
import com.fotoalpha.appointmentsservice.ResponseRequest.GetAllAppointmentsResponse;
import com.fotoalpha.appointmentsservice.ResponseRequest.GetAppointmentByIdResponse;
import com.fotoalpha.appointmentsservice.ResponseRequest.SavePairAppointmentRequest;
import com.fotoalpha.appointmentsservice.ResponseRequest.SaveWeddingAppointmentRequest;
import com.fotoalpha.appointmentsservice.Service.appService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final appService appService;
    private final AppRepo repo;

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

    @GetMapping("/getAllAppointments")
    public ResponseEntity<GetAllAppointmentsResponse> getAllAppointments(@RequestParam String userId) {
        return new ResponseEntity<>(appService.getAllAppointmentByUserId(userId ), HttpStatus.OK);
    }

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
    public ResponseEntity<Boolean> saveWeddingAppointment(@RequestBody SaveWeddingAppointmentRequest req, @RequestParam("uid") String uid){
        return new ResponseEntity<>(appService.saveWeddingAppointment(req, uid), HttpStatus.OK);
    }
}
