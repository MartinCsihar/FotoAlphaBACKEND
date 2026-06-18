package com.fotoalpha.userservice.Repo;

import com.fotoalpha.userservice.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    @Query(value = "select * from users where username = ?1",nativeQuery = true)
    Optional<User> findByUsername(String username);

    @Query(value = "select * from users where userid = ?1",nativeQuery = true)
    Optional<User> findByUserID(String userID);

    Optional<User> findByEmail(String email);
    @Query(value = "select count(*) from (select  userid from users where userid <> '#A4PKQXABHX')", nativeQuery = true)
    Integer countDistinct();

    @Query(value = "select sum(number_of_photos) from users", nativeQuery = true)
    Integer countPhotos();

    @Query(value = "select number_of_photos from users where userid = ?1", nativeQuery = true)
    Integer countUserPhotos(String uid);

    @Query(value = "select number_of_videos from users where userid = ?1", nativeQuery = true)
    Integer countUserVideos(String uid);


    @Query(value = "select * from users where userid <> '#A4PKQXABHX'", nativeQuery = true)
    List<User> getUsers();

    @Query(value = "select sum(number_of_videos) from users", nativeQuery = true)
    Integer countVideos();

}
