package com.fotoalpha.userservice.Repo;

import com.fotoalpha.userservice.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

    Optional<User> findByUserID(String userID);
    Optional<User> findByEmail(String email);
    @Query(value = "select count(*) from (select  userid from users where userid <> 'MARTIN_ADMINISTRATOR')", nativeQuery = true)
    Integer countDistinct();

    @Query(value = "select sum(number_of_photos) from users", nativeQuery = true)
    Integer countPhotos();

    @Query(value = "select * from users where userid <> 'MARTIN_ADMINISTRATOR'", nativeQuery = true)
    List<User> getUsers();

    @Query(value = "select sum(number_of_videos) from users", nativeQuery = true)
    Integer countVideos();
}
