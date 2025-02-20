package com.lunionlab.turbo_restaurant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "boisson")
@NoArgsConstructor
@Getter
@Setter
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
