package com.lunionlab.turbo_restaurant.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.model.OpeningHoursModel;
import com.lunionlab.turbo_restaurant.model.RestaurantModel;

import java.util.List;

public interface OpeningHourRepo extends JpaRepository<OpeningHoursModel, UUID> {
    Optional<OpeningHoursModel> findFirstByDayOfWeekAndRestaurantAndDeletedFalse(String dayOfWeek,
            RestaurantModel restaurant);

    List<OpeningHoursModel> findByRestaurantAndDeletedFalse(RestaurantModel restaurant);
}
