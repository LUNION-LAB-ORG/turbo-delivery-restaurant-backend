package com.lunionlab.turbo_restaurant.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accompagnement")
@NoArgsConstructor
@Getter
@Setter
public class AccompagnementModel extends BaseModel {
    private String libelle;
    private Long price;
    @ManyToOne
    private RestaurantModel restaurant;
    @Column(name = "is_free", columnDefinition = "BOOLEAN DEFAULT false", nullable = false)
    private boolean free;

    public AccompagnementModel(String libelle, Long price, RestaurantModel restaurant, boolean free) {
        this.libelle = libelle;
        this.price = price;
        this.restaurant = restaurant;
        this.free = free;
    }
}
