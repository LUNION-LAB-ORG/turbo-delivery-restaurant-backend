package com.lunionlab.turbo_restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.model.OptionPlatModel;
import com.lunionlab.turbo_restaurant.model.OptionValeurModel;
import java.util.UUID;

public interface OptionValeurRepo extends JpaRepository<OptionValeurModel, UUID> {
    Boolean existsByValeurAndOptionPlatModelAndDeleted(String valeur, OptionPlatModel optionPlat, Boolean deleted);
}
