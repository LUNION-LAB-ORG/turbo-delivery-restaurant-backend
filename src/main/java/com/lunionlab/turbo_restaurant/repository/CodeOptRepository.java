package com.lunionlab.turbo_restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.model.CodeOptModel;
import java.util.UUID;
import java.util.Optional;

public interface CodeOptRepository extends JpaRepository<CodeOptModel, UUID> {
    Boolean existsByCodeAndDeleted(String code, Boolean deleted);

    Optional<CodeOptModel> findFirstByCode(String code);
}
