package com.lunionlab.turbo_restaurant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.entities.OptionPlatModel;
import com.lunionlab.turbo_restaurant.entities.OptionValeurModel;

import java.util.Optional;
import java.util.UUID;

public interface OptionValeurRepo extends JpaRepository<OptionValeurModel, UUID> {
    Optional<OptionValeurModel> findFirstByValeurAndOptionPlatModelAndDeleted(String valeur, OptionPlatModel optionPlat,
            Boolean deleted);

    Optional<OptionValeurModel> findFirstByValeurAndDeletedFalse(String value);
}
