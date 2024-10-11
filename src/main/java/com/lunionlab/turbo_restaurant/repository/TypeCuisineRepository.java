package com.lunionlab.turbo_restaurant.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.model.TypeCuisineModel;

public interface TypeCuisineRepository extends JpaRepository<TypeCuisineModel, UUID> {
    Optional<TypeCuisineModel> findFirstByLibelleAndDeleted(String libelle, Boolean deleted);

    List<TypeCuisineModel> findAllByDeleted(Boolean deleted);
}
