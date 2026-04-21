package com.lunionlab.turbo_restaurant.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "contact_restaurant")
@NoArgsConstructor
public class ContactRestaurantModel extends BaseModel {

    private String nom;
    private String telephone;

    @ManyToOne
    @JsonBackReference
    private RestaurantModel restaurant;

    public ContactRestaurantModel(String nom, String telephone, RestaurantModel restaurant) {
        this.nom = nom;
        this.telephone = telephone;
        this.restaurant = restaurant;
    }
}
