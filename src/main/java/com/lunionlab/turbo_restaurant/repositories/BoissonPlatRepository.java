package com.lunionlab.turbo_restaurant.repositories;

import java.util.List;
// import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.entities.BoissonPlatModel;
import com.lunionlab.turbo_restaurant.entities.PlatModel;

public interface BoissonPlatRepository extends JpaRepository<BoissonPlatModel, UUID> {
    List<BoissonPlatModel> findByPlatAndDeleted(PlatModel plat, Boolean deleted);
}
