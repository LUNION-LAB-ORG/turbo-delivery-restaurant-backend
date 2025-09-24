package com.lunionlab.turbo_restaurant.response;

import com.lunionlab.turbo_restaurant.model.RestaurantModel;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationWebhookResponse {

    private String url;
    private String description;
    private String alias;
}
