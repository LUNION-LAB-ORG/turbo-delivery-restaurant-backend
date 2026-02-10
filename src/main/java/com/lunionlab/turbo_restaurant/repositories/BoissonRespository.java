package com.lunionlab.turbo_restaurant.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.entities.BoissonModel;
import com.lunionlab.turbo_restaurant.entities.RestaurantModel;

public interface BoissonRespository extends JpaRepository<BoissonModel, UUID> {
    Boolean existsByLibelleAndVolumeAndDeleted(String libelle, Double volume, Boolean deleted);

    Optional<BoissonModel> findFirstByIdAndDeleted(UUID id, Boolean deleted);

    List<BoissonModel> findAllByDeleted(Boolean deleted);

    List<BoissonModel> findByRestaurantAndDeletedFalse(RestaurantModel restaurant);
}
