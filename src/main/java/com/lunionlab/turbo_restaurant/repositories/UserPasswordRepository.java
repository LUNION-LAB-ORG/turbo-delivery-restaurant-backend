package com.lunionlab.turbo_restaurant.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.entities.UserModel;
import com.lunionlab.turbo_restaurant.entities.UserPasswordModel;

public interface UserPasswordRepository extends JpaRepository<UserPasswordModel, UUID> {
    List<UserPasswordModel> findTop5ByUserAndDeletedOrderByDateCreationDesc(UserModel user, Boolean deleted);
}
