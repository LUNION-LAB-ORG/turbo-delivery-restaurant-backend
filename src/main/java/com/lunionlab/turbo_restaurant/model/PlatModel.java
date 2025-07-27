package com.lunionlab.turbo_restaurant.model;

import com.lunionlab.turbo_restaurant.Enums.AvailableEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "plat")
@NoArgsConstructor
@Getter
@Setter
public class PlatModel extends BaseModel {
    private String libelle;
    private String description;
    private Boolean disponible = AvailableEnum.YES;
    private String cookTime;
    private Long price;
    private String imageUrl;
    @ManyToOne
    private RestaurantModel restaurant;
    @ManyToOne
    private CollectionModel collection;

    public PlatModel(String libelle, String description, Long price, String imageUrl,
            RestaurantModel restaurant, CollectionModel collection) {
        this.libelle = libelle;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.restaurant = restaurant;
        this.collection = collection;
    }

}
