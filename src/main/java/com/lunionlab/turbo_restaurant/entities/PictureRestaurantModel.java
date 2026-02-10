package com.lunionlab.turbo_restaurant.entities;


import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
@Table(name = "picture_restaurant")
@NoArgsConstructor
public class PictureRestaurantModel extends BaseModel {
    private String pictureUrl;
    @ManyToOne
    @JsonBackReference
    private RestaurantModel restaurant;

    public PictureRestaurantModel(String pictureUrl, RestaurantModel restaurant) {
        this.pictureUrl = pictureUrl;
        this.restaurant = restaurant;
    }
}
