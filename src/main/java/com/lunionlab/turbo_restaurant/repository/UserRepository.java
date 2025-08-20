package com.lunionlab.turbo_restaurant.repository;

import com.lunionlab.turbo_restaurant.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

    Optional<UserModel> findFirstByUsernameAndDeleted(String username, Boolean deleted);

    Optional<UserModel> findFirstByUsernameAndStatusAndDeleted(String username, Integer status, Boolean deleted);

    Optional<UserModel> findFirstByEmail(String email);

    boolean existsByEmail(String email);

    Optional<UserModel> findFirstByEmailAndDeleted(String email, Boolean deleted);

    List<UserModel> findAllByRestaurantIdAndDeletedFalse(UUID restoId);

    Optional<UserModel> findByApiKeyAndDeleted(String apiKey, Boolean deleted);

}
