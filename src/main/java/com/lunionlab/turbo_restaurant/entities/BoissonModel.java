package com.lunionlab.turbo_restaurant.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "boisson")
@NoArgsConstructor
@Data
public class BoissonModel extends BaseModel {
    private String libelle;
    private Long price;
    private Double volume;
    @ManyToOne
    private RestaurantModel restaurant;

    public BoissonModel(String libelle, Long price, Double volume, RestaurantModel restaurant) {
        this.libelle = libelle;
        this.price = price;
        this.volume = volume;
        this.restaurant = restaurant;
    }

}
