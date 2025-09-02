package com.lunionlab.turbo_restaurant.dto;

import java.util.UUID;

public record NotificationsWebhookDto(
        String url,
        String description,
        UUID restaurantId
) {
}
