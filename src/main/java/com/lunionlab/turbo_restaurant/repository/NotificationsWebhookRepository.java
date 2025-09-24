package com.lunionlab.turbo_restaurant.repository;

import com.lunionlab.turbo_restaurant.model.NotificationsWebhookModel;
import java.util.List;
import java.util.UUID;

import com.lunionlab.turbo_restaurant.model.RestaurantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NotificationsWebhookRepository extends JpaRepository<NotificationsWebhookModel, UUID> {

    boolean existsById(UUID webhookId);

    List<NotificationsWebhookModel> findAllByDeleted(Boolean deleted);

    Optional<NotificationsWebhookModel> findFirstByIdAndDeleted(UUID restoId, Boolean deleted);

    List<NotificationsWebhookModel> findByRestaurantAndDeleted(RestaurantModel restaurant,  Boolean deleted);
}
