package com.lunionlab.turbo_restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.model.OptionPlatModel;
import com.lunionlab.turbo_restaurant.model.PlatModel;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface OptionPlatRepo extends JpaRepository<OptionPlatModel, UUID> {
    Boolean existsByLibelleAndPlatAndDeleted(String libelle, PlatModel plat, Boolean deleted);

    List<OptionPlatModel> findAllByDeleted(Boolean deleted);

    List<OptionPlatModel> findByPlatAndDeletedFalse(PlatModel platM);

    Optional<OptionPlatModel> findFirstByIdAndDeleted(UUID id, Boolean deleted);

    Optional<OptionPlatModel> findByLibelleAndPlatAndDeleted(String libelle, PlatModel plat, Boolean deleted);

    Optional<OptionPlatModel> findFirstByLibelleAndPlatAndDeleted(String libelle, PlatModel plat, Boolean deleted);
}
