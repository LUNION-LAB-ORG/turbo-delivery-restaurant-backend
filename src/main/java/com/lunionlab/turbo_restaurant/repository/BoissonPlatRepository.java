package com.lunionlab.turbo_restaurant.repository;

import java.util.List;
// import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

// import com.lunionlab.turbo_restaurant.model.BoissonModel;
import com.lunionlab.turbo_restaurant.model.BoissonPlatModel;
import com.lunionlab.turbo_restaurant.model.PlatModel;

public interface BoissonPlatRepository extends JpaRepository<BoissonPlatModel, UUID> {
    List<BoissonPlatModel> findByPlatAndDeleted(PlatModel plat, Boolean deleted);
}
