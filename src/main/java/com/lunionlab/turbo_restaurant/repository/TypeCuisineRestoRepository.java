package com.lunionlab.turbo_restaurant.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.model.RestaurantModel;
import com.lunionlab.turbo_restaurant.model.TypeCuisineRestaurantModel;

public interface TypeCuisineRestoRepository extends JpaRepository<TypeCuisineRestaurantModel, UUID> {
    List<TypeCuisineRestaurantModel> findByRestaurantAndDeleted(RestaurantModel restaurant, Boolean deleted);
}
