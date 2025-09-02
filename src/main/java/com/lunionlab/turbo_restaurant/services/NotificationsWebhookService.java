package com.lunionlab.turbo_restaurant.services;

import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.Enums.StatusEnum;
import com.lunionlab.turbo_restaurant.dto.NotificationsWebhookDto;
import com.lunionlab.turbo_restaurant.exception.ObjetNonTrouveException;
import com.lunionlab.turbo_restaurant.model.NotificationsWebhookModel;
import com.lunionlab.turbo_restaurant.model.RestaurantModel;
import com.lunionlab.turbo_restaurant.repository.NotificationsWebhookRepository;
import com.lunionlab.turbo_restaurant.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NotificationsWebhookService {

    private final NotificationsWebhookRepository webhookRepository;
    private final RestaurantRepository restaurantRepository;

    public NotificationsWebhookService(NotificationsWebhookRepository webhookRepository, RestaurantRepository restaurantRepository) {
        this.webhookRepository = webhookRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public List<NotificationsWebhookModel> lister() {
        return webhookRepository.findAllByDeleted(DeletionEnum.NO);
    }

    @Transactional
    public NotificationsWebhookModel enregistrer(NotificationsWebhookDto dto) {
        RestaurantModel restaurant = validateRestaurant(dto.restaurantId());
        NotificationsWebhookModel webhook = new NotificationsWebhookModel();
        webhook.setUrl(dto.url());
        webhook.setDescription(dto.description());
        webhook.setRestaurant(restaurant);
        webhook = webhookRepository.save(webhook);
        log.info("Webhook créé pour restaurant {}", restaurant.getId());
        return webhook;
    }

    @Transactional
    public NotificationsWebhookModel modifier(UUID id, NotificationsWebhookDto dto) {
        NotificationsWebhookModel webhook = webhookRepository.findById(id)
                .orElseThrow(() -> new ObjetNonTrouveException("Webhook non trouvé"));
        RestaurantModel restaurant = validateRestaurant(dto.restaurantId());
        webhook.setUrl(dto.url());
        webhook.setDescription(dto.description());
        webhook.setRestaurant(restaurant);
        webhook = webhookRepository.save(webhook);
        log.info("Webhook {} mis à jour", id);
        return webhook;
    }

    @Transactional
    public void supprimer(UUID id) {
        if (!webhookRepository.existsById(id)) {
            throw new ObjetNonTrouveException("Webhook non trouvé");
        }
        webhookRepository.deleteById(id);
        log.info("Webhook {} supprimé", id);
    }

    private RestaurantModel validateRestaurant(UUID restoId) {
        List<Integer> statusAllow = List.of(StatusEnum.DEFAULT_DESABLE, StatusEnum.DEFAULT_ENABLE);
        Optional<RestaurantModel> restaurantOpt = restaurantRepository.findFirstByIdAndStatusInAndDeleted(restoId, statusAllow, DeletionEnum.NO);
        if (restaurantOpt.isEmpty()) {
            log.error("Restaurant introuvable pour ID {}", restoId);
            throw new ObjetNonTrouveException("Cet restaurant n'existe pas !");
        }
        return restaurantOpt.get();
    }
}
