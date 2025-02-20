package com.lunionlab.turbo_restaurant.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.model.CollectionModel;

public interface CollectionRepository extends JpaRepository<CollectionModel, UUID> {
    Optional<CollectionModel> findFirstByIdAndDeleted(UUID id, Boolean deleted);

    Boolean existsByLibelleAndDeleted(String libelle, Boolean deleted);

    Optional<CollectionModel> findFirstByLibelleAndDeleted(String libelle, Boolean deleted);

    List<CollectionModel> findAll();
}
