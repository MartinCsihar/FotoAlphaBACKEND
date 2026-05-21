package com.fotoalpha.appointmentsservice.Service;

import com.fotoalpha.appointmentsservice.Entity.Address;
import com.fotoalpha.appointmentsservice.Entity.Appointments;
import com.fotoalpha.appointmentsservice.Entity.Bundles;
import com.fotoalpha.appointmentsservice.Enums.AppointmentType;
import com.fotoalpha.appointmentsservice.Kafka.Consumer;
import com.fotoalpha.appointmentsservice.Kafka.Producer;
import com.fotoalpha.appointmentsservice.KafkaEvents.AppointmentCreatedEvent;
import com.fotoalpha.appointmentsservice.KafkaEvents.GetUserDataEvent;
import com.fotoalpha.appointmentsservice.KafkaEvents.GetUserDataEventResponse;
import com.fotoalpha.appointmentsservice.KafkaEvents.SaveAddressEvent;
import com.fotoalpha.appointmentsservice.Repo.AppRepo;
import com.fotoalpha.appointmentsservice.Repo.BundleRepo;
import com.fotoalpha.appointmentsservice.ResponseRequest.GetAllAppointmentsResponse;
import com.fotoalpha.appointmentsservice.ResponseRequest.GetAppointmentByIdResponse;
import com.fotoalpha.appointmentsservice.ResponseRequest.SavePairAppointmentRequest;
import com.fotoalpha.appointmentsservice.ResponseRequest.SaveWeddingAppointmentRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class appService {
    private final AppRepo appRepo;
    private final BundleRepo bundleRepo;
    private final Producer producer;
    private final Consumer consumer;


    public GetAllAppointmentsResponse getAllAppointmentByUserId(String uid) {
        List<Appointments> querriedApps = appRepo.findAllByUserId(uid);
        return GetAllAppointmentsResponse.builder().appointments(querriedApps).build();
    }

    public GetAppointmentByIdResponse getAppointmentByAppointmentIdAndUserId(String appId, String uid) {
        Appointments querriedApp = appRepo.findByIdAndUserId(appId, uid);
        return GetAppointmentByIdResponse.builder().querriedAppointment(querriedApp).build();
    }

    @Transactional
    public Boolean cancelAppointment(String uid, String appointmentId) {
        Appointments app = appRepo.findByIdAndUserId(appointmentId, uid);
        LocalDate now = LocalDate.now();
        LocalDate appointmentDate = app.getAppointmentDate();
        if (now.plusWeeks(2).isAfter(appointmentDate)) {
            return false;
        }
        int rowsModified = appRepo.cancelAppointment(appointmentId, uid);
        return true;

    }

    public Boolean savePairAppointment(SavePairAppointmentRequest req, String uid) throws ExecutionException, InterruptedException, TimeoutException {
        Bundles bundle = bundleRepo.findById(Long.valueOf(req.getBundleId())).orElse(null);
        Appointments newApp = Appointments.builder()
                .userId(uid)
                .appointmentDate(req.getAppointmentDate())
                .appointmentTime(req.getAppointmentTime())
                .type(AppointmentType.STANDARD)
                .bundle(bundle)
                .build();
        appRepo.save(newApp);
        SaveAddressEvent saveAddress = SaveAddressEvent.builder()
                .addressId(newApp.getAddressId())
                .isPairLocations(true)
                .pairLocations(bundle.getPairLocations())
                .userID(uid)
                .city("Budapest")
                .build();
        producer.sendSaveAddressEvent(saveAddress);

//        String correlationId = UUID.randomUUID().toString();
//        GetUserDataEvent gude = GetUserDataEvent.builder()
//                .UserID(uid)
//                .correlationId(correlationId)
//                .build();
//        GetUserDataEventResponse guder = producer.requestUserData(gude);

        AppointmentCreatedEvent appCreatedEvent = AppointmentCreatedEvent.builder()
                .pairLocations(bundle.getPairLocations())
                .appointmentId(newApp.getId())
                .orderDate(LocalDate.now())
                .appointmentDate(req.getAppointmentDate())
                .appointmentTime(req.getAppointmentTime())
                .price(bundle.getBundlePrice())
                .bundleName(bundle.getBundleSubType().toString())
                .appointmentType(newApp.getType().toString())
                .state("Elfogadva")
                .userEmail("martincsihar@gmail.com")
                .firstName("Csihar")
                .lastName("Martin")
                .phoneNumber("+36 30 860 2406")
//                .userEmail(guder.email())
//                .firstName(guder.firstName())
//                .lastName(guder.lastName())
//                .phoneNumber(guder.phoneNumber())
                .build();

        producer.sendAppCreatedEvent(appCreatedEvent);
        return true;
    }

    public Boolean saveWeddingAppointment(SaveWeddingAppointmentRequest req, String uid) {
        Bundles bundle = bundleRepo.findById(Long.valueOf(req.getBundle())).orElse(null);
        Appointments newApp = Appointments.builder()
                .appointmentDate(req.getAppointmentDate())
                .appointmentTime(req.getAppointmentTime())
                .type(AppointmentType.STANDARD)
                .bundle(bundle)
                .userId(uid)
                .build();
        appRepo.save(newApp);
        Address address = req.getAddress();
        SaveAddressEvent sae = SaveAddressEvent.builder()
                .addressId(newApp.getAddressId())
                .isPairLocations(false)
                .postalCode(address.getPostalCode())
                .city(address.getCity())
                .streetName(address.getStreetName())
                .streetType(address.getStreetType())
                .houseNumber(address.getHouseNumber())
                .userID(uid)
                .build();
        producer.sendSaveAddressEvent(sae);

//        String correlationId = UUID.randomUUID().toString();
//        GetUserDataEvent gude = GetUserDataEvent.builder()
//                .UserID(uid)
//                .correlationId(correlationId)
//                .build();
//        GetUserDataEventResponse guder = producer.requestUserData(gude);

        AppointmentCreatedEvent appCreatedEvent = AppointmentCreatedEvent.builder()
                .postalCode(address.getPostalCode())
                .streetName(address.getStreetName())
                .streetType(address.getStreetType())
                .houseNumber(address.getHouseNumber())
                .appointmentId(newApp.getId())
                .orderDate(LocalDate.now())
                .appointmentDate(req.getAppointmentDate())
                .appointmentTime(req.getAppointmentTime())
                .price(bundle.getBundlePrice())
                .bundleName(bundle.getBundleSubType().toString())
                .appointmentType(newApp.getType().toString())
                .state("Elfogadva")
                .userEmail("martincsihar@gmail.com")
                .firstName("Csihar")
                .lastName("Martin")
                .phoneNumber("+36 30 860 2406")
//                .userEmail(guder.email())
//                .firstName(guder.firstName())
//                .lastName(guder.lastName())
//                .phoneNumber(guder.phoneNumber())
                .build();

        producer.sendAppCreatedEvent(appCreatedEvent);
        return true;
    }
}
