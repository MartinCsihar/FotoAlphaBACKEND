package com.fotoalpha.appointmentsservice.Service;

import com.fotoalpha.appointmentsservice.Entity.*;
import com.fotoalpha.appointmentsservice.Enums.AppointmentType;
import com.fotoalpha.appointmentsservice.Enums.Status;
import com.fotoalpha.appointmentsservice.Kafka.Consumer;
import com.fotoalpha.appointmentsservice.Kafka.Producer;
import com.fotoalpha.appointmentsservice.KafkaEvents.*;
import com.fotoalpha.appointmentsservice.Repo.AppRepo;
import com.fotoalpha.appointmentsservice.Repo.BundleRepo;
import com.fotoalpha.appointmentsservice.Repo.EventRepo;
import com.fotoalpha.appointmentsservice.Repo.PersonalBundlesRepo;
import com.fotoalpha.appointmentsservice.ResponseRequest.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Fetch;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class appService {
    private final AppRepo appRepo;
    private final BundleRepo bundleRepo;
    private final Producer producer;
    private final EventRepo eventRepo;
    private final PersonalBundlesRepo personalBundlesRepo;




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

       String correlationId = UUID.randomUUID().toString();
       GetUserDataEvent gude = GetUserDataEvent.builder()
               .UserID(uid)
               .correlationId(correlationId)
               .build();
       GetUserDataEventResponse guder = producer.requestUserData(gude);

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
//                .userEmail("martincsihar@gmail.com")
//                .firstName("Csihar")
//                .lastName("Martin")
//                .phoneNumber("+36 30 860 2406")
                .userEmail(guder.email())
                .firstName(guder.firstName())
                .lastName(guder.lastName())
                .phoneNumber(guder.phoneNumber())
                .build();

        producer.sendAppCreatedEvent(appCreatedEvent);
        return true;
    }

    public Boolean saveWeddingAppointment(SaveWeddingAppointmentRequest req, String uid) throws ExecutionException, InterruptedException, TimeoutException {
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

        String correlationId = UUID.randomUUID().toString();
        GetUserDataEvent gude = GetUserDataEvent.builder()
                .UserID(uid)
                .correlationId(correlationId)
                .build();
        GetUserDataEventResponse guder = producer.requestUserData(gude);

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
//                .userEmail("martincsihar@gmail.com")
//                .firstName("Csihar")
//                .lastName("Martin")
//                .phoneNumber("+36 30 860 2406")
                .userEmail(guder.email())
                .firstName(guder.firstName())
                .lastName(guder.lastName())
                .phoneNumber(guder.phoneNumber())
                .build();

        producer.sendAppCreatedEvent(appCreatedEvent);
        return true;
    }
    @Transactional
    public boolean saveOwnMadeApp(SaveOwnMadeWeddingAppointmentRequest req, String uid) throws ExecutionException, InterruptedException, TimeoutException {
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

        String correlationId = UUID.randomUUID().toString();
        GetUserDataEvent gude = GetUserDataEvent.builder()
                .UserID(uid)
                .correlationId(correlationId)
                .build();
        GetUserDataEventResponse guder = producer.requestUserData(gude);
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
//                .userEmail("martincsihar@gmail.com")
//                .firstName("Csihar")
//                .lastName("Martin")
//                .phoneNumber("+36 30 860 2406")
                .userEmail(guder.email())
                .firstName(guder.firstName())
                .lastName(guder.lastName())
                .phoneNumber(guder.phoneNumber())
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

    public Map<String, Integer> getTotalIncome() {
        Map<String, Integer> totalIncome = new HashMap<>();
        Integer ownMade =appRepo.getTotalIncomeOwnMade() != null ? appRepo.getTotalIncomeOwnMade() : 0;
        Integer pairWedding = appRepo.getTotalIncomePairWedding() != null ? appRepo.getTotalIncomePairWedding() : 0;
        totalIncome.put("own_made_wedding", ownMade);
        totalIncome.put("pair_wedding",pairWedding);
        totalIncome.put("total_income", ownMade + pairWedding);
        return totalIncome;
    }

    public Map<String, Integer> countAppointments() {
        Map<String, Integer> appCounts = new HashMap<>();
        int pair = appRepo.countAppointmentsByBundleType("Pair");
        int wedding = appRepo.countAppointmentsByBundleType("Wedding");
        appCounts.put("pair", pair);
        appCounts.put("wedding", wedding);
        return appCounts;
    }
    @Transactional
    public Boolean modifyAppDetails(String appId, String uid, AdminModifyDetailsRequest req) {
        Appointments foundApp  = appRepo.findById(appId).orElse(null);
        LocalDate reqDate = req.getAppointmentDate();
        LocalTime reqTime = req.getAppointmentTime();
        if (foundApp != null) {
            if (reqDate != null) {
                appRepo.updateAppDate(reqDate, uid, appId);
                return true;
            };
            if (reqTime != null) {
                appRepo.updateAppTime(reqTime, uid, appId);
                return true;
            };
            return false;
        }
        return false;
    }
    @Transactional
    public Boolean accomplishAppointment(String appid, String uid) {
        appRepo.updateStatus(String.valueOf(Status.completed), uid, appid );
        return true;
    }

    public Boolean deleteAppointment(String appid) {
        Appointments foundApp  = appRepo.findById(appid).orElse(null);
        if (foundApp != null) {
            if (foundApp.getPresonalBundle() != null) {
                appRepo.deleteById(appid);
                personalBundlesRepo.delete(foundApp.getPresonalBundle());
                return true;
            }
            else{
                appRepo.deleteById(appid);
                return true;
            }
        }
        return false;
    }
    public List<Appointments> getAllAppointmentsByUserId(String uid) {
        return appRepo.findAllByUserId(uid);
    }

    public Appointments getAllAppointmentsOfUserByAppId(String appid, String uid) {
        return appRepo.findByIdAndUserId(appid, uid);
    }

    public List<Appointments> allAppointments() {
        return appRepo.findAll();
    }

    public Integer countClients() {
        return appRepo.countClients();
    }

    public List<User> fetchUsers() throws ExecutionException, InterruptedException, TimeoutException {
        String correlationId = UUID.randomUUID().toString();
        FetchUsersEvent fue = new FetchUsersEvent(List.of(null),  correlationId);
        return producer.sendFetchUsersEvent(fue).users();
    }

    public void getRequestedAppointments(AppInfoReqEvent appInfoReqEvent) {
        List<Appointments> querriedApps = appRepo.findAllById(appInfoReqEvent.appIds());
        List<AppointmentResponse> appointmentResponses = new ArrayList<>();
        for (Appointments app : querriedApps) {
            AppointmentResponse ar = AppointmentResponse.builder()
                    .type(app.getType().toString())
                    .bunldeSubType(app.getBundle().getBundleSubType().toString())
                    .userId(app.getUserId())
                    .appId(app.getId())
                    .build();
            appointmentResponses.add(ar);
        }
        AppInfoResEvent appInfoResEvent = new AppInfoResEvent(appInfoReqEvent.correlationId(), appointmentResponses);
        producer.sendAppInfoResEvent(appInfoResEvent);
    }
}
