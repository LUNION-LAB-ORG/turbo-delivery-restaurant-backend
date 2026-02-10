package com.lunionlab.turbo_restaurant.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.entities.RestaurantModel;
import com.lunionlab.turbo_restaurant.entities.UserOrderM;

public interface UserOrderRepo extends JpaRepository<UserOrderM, UUID> {
    Boolean existsByOrderIdAndOrderState(String orderId, String orderState);

    Page<UserOrderM> findByRestaurantAndDeletedFalse(RestaurantModel restaurant, Pageable page);
}
