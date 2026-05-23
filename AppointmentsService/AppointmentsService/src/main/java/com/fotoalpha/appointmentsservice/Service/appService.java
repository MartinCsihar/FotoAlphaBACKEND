package com.fotoalpha.appointmentsservice.Service;

import com.fotoalpha.appointmentsservice.Entity.*;
import com.fotoalpha.appointmentsservice.Enums.AppointmentType;
import com.fotoalpha.appointmentsservice.Enums.Status;
import com.fotoalpha.appointmentsservice.Kafka.Consumer;
import com.fotoalpha.appointmentsservice.Kafka.Producer;
import com.fotoalpha.appointmentsservice.KafkaEvents.AppointmentCreatedEvent;
import com.fotoalpha.appointmentsservice.KafkaEvents.SaveAddressEvent;
import com.fotoalpha.appointmentsservice.Repo.AppRepo;
import com.fotoalpha.appointmentsservice.Repo.BundleRepo;
import com.fotoalpha.appointmentsservice.Repo.EventRepo;
import com.fotoalpha.appointmentsservice.Repo.PersonalBundlesRepo;
import com.fotoalpha.appointmentsservice.ResponseRequest.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
    private final EventRepo eventRepo;
    private final PersonalBundlesRepo personalBundlesRepo;


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
    @Transactional
    public boolean saveOwnMadeApp(SaveOwnMadeWeddingAppointmentRequest req, String uid) {
        List<Events> choosenEvents = eventRepo.findAllById((req.getEventIds()));
        int bundlePrice = getBundlePrice(req, choosenEvents);

        PersonalBundles pb = PersonalBundles.builder()
                .rawPhotos(req.getRawPhotos())
                .lightPaintPhotos(req.getLightPaintPhotos())
                .editedPhotos(req.getEditedPhotos())
                .instantPhotos(req.getInstantPhotos())
                .lightPaintPhotos(req.getLightPaintPhotos())
                .bundlePrice(bundlePrice)
                .events(choosenEvents)
                .build();
        Appointments newOwnMadeApp = Appointments.builder()
                .appointmentDate(req.getAppointmentDate())
                .appointmentTime(req.getAppointmentTime())
                .type(AppointmentType.PERSONAL)
                .status(Status.pending)
                .presonalBundle(pb)
                .userId(uid)
                .build();
        personalBundlesRepo.save(pb);
        appRepo.save(newOwnMadeApp);
        Address address = req.getAddress();
        SaveAddressEvent sae = SaveAddressEvent.builder()
                .addressId(newOwnMadeApp.getAddressId())
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
                .appointmentId(newOwnMadeApp.getId())
                .orderDate(LocalDate.now())
                .appointmentDate(req.getAppointmentDate())
                .appointmentTime(req.getAppointmentTime())
                .price(pb.getBundlePrice())
                .bundleName("SAJÁT")
                .appointmentType("PERSONAL")
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

    private static int getBundlePrice(SaveOwnMadeWeddingAppointmentRequest req, List<Events> choosenEvents) {
        int bundlePrice = 100000;
        for (Events event : choosenEvents) {
            bundlePrice += event.getEventPrice();
        }
        switch (choosenEvents.size()) {
            case 1: bundlePrice += 5000; break;
            case 2: bundlePrice += 3*5000; break;
            case 3: bundlePrice += 5*5000; break;
            case 4: bundlePrice += 8*5000; break;
            case 5: bundlePrice += 60000; break;
        }
        bundlePrice += (req.getRawPhotos() / 50) * 3000;
        bundlePrice += (req.getInstantPhotos() / 10) * 10000;
        bundlePrice += (req.getEditedPhotos() / 10) * 3000;
        bundlePrice += req.getLightPaintPhotos() == true ?  10000 : 0;
        return bundlePrice;
    }
}
