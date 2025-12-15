package com.lunionlab.turbo_restaurant.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class RestaurantSearchDto {
    
    private UUID id;
    private String name;
    private String image;

    public RestaurantSearchDto(UUID id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }
}
