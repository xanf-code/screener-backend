package com.screener.user_service_backend.repository;

import com.screener.user_service_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByVerificationCode(Integer verificationCode);

    Optional<User> findByResetPasswordToken(String resetPasswordToken);
}
