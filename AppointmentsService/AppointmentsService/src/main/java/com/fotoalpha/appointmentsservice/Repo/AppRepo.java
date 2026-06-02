package com.fotoalpha.appointmentsservice.Repo;

import com.fotoalpha.appointmentsservice.Entity.Appointments;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppRepo extends JpaRepository<Appointments, String> {
    List<Appointments> findAllByUserId(String userId);
    Appointments findByIdAndUserId(String appId, String userId);

    @Modifying
    @Query(value = "UPDATE Appointments SET status = 'cancelled' WHERE  id = ?1 AND user_id = ?2", nativeQuery = true)
    int cancelAppointment(String appid, String userid);

    @Query(value = "select sum(b.bundle_price) as TotalIncome from appointments a inner join bundles b using(bundle_id)",nativeQuery = true)
    Integer getTotalIncomePairWedding();

    @Query(value = "select sum(pb.bundle_price) as TotalIncome from appointments a inner join personal_bundles pb \n" +
            "on a.personal_bundle_id = pb.id;",nativeQuery = true)
    Integer getTotalIncomeOwnMade();

    @Query(value = "select count(*) as WeddingCount from appointments a inner join bundles b on a.bundle_id = b.bundle_id \n" +
            "where b.bundle_type = ?1;",nativeQuery = true)
    int countAppointmentsByBundleType(String type);

    @Modifying
    @Query(value = "update appointments set appointment_date = ?1 where user_id = ?2 and id = ?3;",nativeQuery = true)
    void updateAppDate(LocalDate date, String uid, String appid);

    @Modifying
    @Query(value = "update appointments set appointment_time = ?1 where user_id = ?2 and id = ?3;",nativeQuery = true)
    void updateAppTime(LocalTime time, String uid, String appid);

    @Modifying
    @Query(value = "update appointments set status = ?1 where user_id = ?2 and id = ?3;",nativeQuery = true)
    void updateStatus(String status, String uid, String appid);

    @Query(value = "select count(*) from (select distinct user_id from appointments);",nativeQuery = true)
    Integer countClients();
}
