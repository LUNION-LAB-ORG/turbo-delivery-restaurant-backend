package com.lunionlab.turbo_restaurant.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.lunionlab.turbo_restaurant.entities.OrderItemModel;

public interface OrderItemRepo extends JpaRepository<OrderItemModel, UUID> {
}
