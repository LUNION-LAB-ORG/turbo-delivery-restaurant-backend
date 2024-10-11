package com.lunionlab.turbo_restaurant.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.model.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, UUID> {
    Optional<RoleModel> findFirstByLibelleAndDeleted(String libelle, Boolean deleted);

    List<RoleModel> findAllByDeleted(Boolean deleted);
}
