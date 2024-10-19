package com.lunionlab.turbo_restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.model.PlatModel;
import java.util.UUID;
import java.util.Optional;

public interface PlatRepository extends JpaRepository<PlatModel, UUID> {
    Optional<PlatModel> findFirstByIdAndDeleted(UUID id, Boolean deleted);
}
