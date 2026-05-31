package com.fotoalpha.userservice.Repo;

import com.fotoalpha.userservice.Entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassowordResetTokenRepo extends JpaRepository<PasswordResetToken, String> {
}
