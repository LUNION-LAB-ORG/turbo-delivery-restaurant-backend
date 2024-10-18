package com.lunionlab.turbo_restaurant.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.model.RestaurantModel;

public interface RestaurantRepository extends JpaRepository<RestaurantModel, UUID> {
    Boolean existsByNomEtablissementAndEmailAndDeleted(String nomEtablissement, String email, Boolean deleted);
}
