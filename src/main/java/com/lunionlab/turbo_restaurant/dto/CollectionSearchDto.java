package com.lunionlab.turbo_restaurant.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class CollectionSearchDto {
    
    private UUID id;
    private String libelle;
    private String description;
    private String picture;
    private String pictureUrl;

    public CollectionSearchDto(UUID id, String libelle, String description, String picture, String pictureUrl) {
        this.id = id;
        this.libelle = libelle;
        this.description = description;
        this.picture = picture;
        this.pictureUrl = pictureUrl;
    }
}
