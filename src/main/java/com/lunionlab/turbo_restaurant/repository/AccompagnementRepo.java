package com.lunionlab.turbo_restaurant.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.model.AccompagnementModel;
import com.lunionlab.turbo_restaurant.model.PlatModel;

public interface AccompagnementRepo extends JpaRepository<AccompagnementModel, UUID> {
//    Boolean existsByLibelleAndPlatModelAndDeleted(String libelle, PlatModel plat, Boolean deleted);

    Optional<AccompagnementModel> findFirstByLibelleAndDeleted(String libelle, Boolean deleted);

//    List<AccompagnementModel> findByPlatModelAndDeleted(PlatModel platModel, Boolean deleted);

    Optional<AccompagnementModel> findFirstByIdAndDeleted(UUID id, Boolean deleted);
}
