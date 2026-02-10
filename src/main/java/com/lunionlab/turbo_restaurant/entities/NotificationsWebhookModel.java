package com.lunionlab.turbo_restaurant.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "notificationswebhooks")
public class NotificationsWebhookModel extends BaseModel {

    @Column(nullable = false, length = 255)
    private String url;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    private RestaurantModel restaurant;
}
