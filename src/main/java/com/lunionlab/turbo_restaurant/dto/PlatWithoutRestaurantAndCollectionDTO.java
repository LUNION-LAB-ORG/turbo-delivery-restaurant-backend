package com.lunionlab.turbo_restaurant.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PlatWithoutRestaurantAndCollectionDTO {
    private UUID id;            // hérité de BaseModel
    private String libelle;
    private String description;
    private Boolean disponible;
    private String cookTime;
    private Long price;
    private String imageUrl;
}
