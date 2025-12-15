package com.lunionlab.turbo_restaurant.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class PlatSearchDto {
    
    private UUID id;
    private String name;
    private double price;
    private String image;
    private UUID restaurantId;
    private String restaurantName;

    public PlatSearchDto(UUID id, String name, double price,
                         String image, UUID restaurantId, String restaurantName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
    }
}
