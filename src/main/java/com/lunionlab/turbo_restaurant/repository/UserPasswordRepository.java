package com.lunionlab.turbo_restaurant.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.model.UserModel;
import com.lunionlab.turbo_restaurant.model.UserPasswordModel;

public interface UserPasswordRepository extends JpaRepository<UserPasswordModel, UUID> {
    List<UserPasswordModel> findTop5ByUserAndDeletedOrderByDateCreationDesc(UserModel user, Boolean deleted);
}
