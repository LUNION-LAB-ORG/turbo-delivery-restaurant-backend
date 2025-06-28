package com.lunionlab.turbo_restaurant.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.model.BoissonModel;
import com.lunionlab.turbo_restaurant.model.RestaurantModel;

public interface BoissonRespository extends JpaRepository<BoissonModel, UUID> {
    Boolean existsByLibelleAndVolumeAndDeleted(String libelle, Double volume, Boolean deleted);

    Optional<BoissonModel> findFirstByIdAndDeleted(UUID id, Boolean deleted);

    List<BoissonModel> findAllByDeleted(Boolean deleted);

    List<BoissonModel> findByRestaurantAndDeletedFalse(RestaurantModel restaurant);

}
