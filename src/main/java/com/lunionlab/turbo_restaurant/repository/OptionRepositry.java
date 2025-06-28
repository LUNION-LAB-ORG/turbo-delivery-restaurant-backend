package com.lunionlab.turbo_restaurant.repository;

import com.lunionlab.turbo_restaurant.model.OptionModel;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author daniel.kouame 2025-06-27
 */
public interface OptionRepositry extends JpaRepository<OptionModel, UUID> {

  Optional<OptionModel> findByLibelleAndRestaurantId(String libelle, UUID id);
}
