package com.lunionlab.turbo_restaurant.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.model.PictureRestaurantModel;
import com.lunionlab.turbo_restaurant.model.RestaurantModel;

public interface PictureRestoRepository extends JpaRepository<PictureRestaurantModel, UUID> {

    List<PictureRestaurantModel> findByRestaurantAndDeleted(RestaurantModel restaurant, Boolean deleted);
}
