package com.lunionlab.turbo_restaurant.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.model.UserOrderM;

public interface UserOrderRepo extends JpaRepository<UserOrderM, UUID> {
    Boolean existsByOrderIdAndOrderState(String orderId, String orderState);
}
