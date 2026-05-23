package com.fotoalpha.appointmentsservice.Repo;

import com.fotoalpha.appointmentsservice.Entity.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppRepo extends JpaRepository<Appointments, String> {
    List<Appointments> findAllByUserId(String userId);
    Appointments findByIdAndUserId(String appId, String userId);

    @Modifying
    @Query(value = "UPDATE Appointments SET status = 'cancelled' WHERE  id = ?1 AND user_id = ?2", nativeQuery = true)
    int cancelAppointment(String appid, String userid);

    @Query(value = "select sum(b.bundle_price) as TotalIncome from appointments a inner join bundles b using(bundle_id)",nativeQuery = true)
    int getTotalIncomePairWedding();

    @Query(value = "select sum(pb.bundle_price) as TotalIncome from appointments a inner join personal_bundles pb \n" +
            "on a.personal_bundle_id = pb.id ;",nativeQuery = true)
    int getTotalIncomeOwnMade();
}
