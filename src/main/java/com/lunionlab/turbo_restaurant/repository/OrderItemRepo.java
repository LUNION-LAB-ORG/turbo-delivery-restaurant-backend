package com.lunionlab.turbo_restaurant.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.model.OrderItemModel;

public interface OrderItemRepo extends JpaRepository<OrderItemModel, UUID> {

}
