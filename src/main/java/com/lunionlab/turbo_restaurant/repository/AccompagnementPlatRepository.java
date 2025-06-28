package com.lunionlab.turbo_restaurant.repository;

import com.lunionlab.turbo_restaurant.model.AccompagnementModel;
import com.lunionlab.turbo_restaurant.model.AccompagnementPlatModel;
import com.lunionlab.turbo_restaurant.model.PlatModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author daniel.kouame 2025-06-27
 */
public interface AccompagnementPlatRepository extends JpaRepository<AccompagnementPlatModel, UUID> {


  List<AccompagnementPlatModel> findByPlatModelAndDeleted(PlatModel platModel, Boolean no);

  boolean existsByPlatModelIdAndAccompagnementModelId(UUID platId, UUID accompagnementId);
}
