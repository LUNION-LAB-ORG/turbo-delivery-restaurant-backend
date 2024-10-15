package com.lunionlab.turbo_restaurant.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    Optional<UserModel> findFirstByUsernameAndDeleted(String username, Boolean deleted);

    Optional<UserModel> findFirstByUsernameAndStatusAndDeleted(String username, Integer status, Boolean deleted);

    Optional<UserModel> findFirstByEmail(String email);
}
