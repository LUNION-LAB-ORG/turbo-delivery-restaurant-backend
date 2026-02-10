package com.lunionlab.turbo_restaurant.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.entities.NotificationsWebhookModel;

import java.util.Optional;

public interface NotificationsWebhookRepository extends JpaRepository<NotificationsWebhookModel, UUID> {

    boolean existsById(UUID webhookId);

    List<NotificationsWebhookModel> findAllByDeleted(Boolean deleted);

    Optional<NotificationsWebhookModel> findFirstByIdAndDeleted(UUID restoId, Boolean deleted);

}
