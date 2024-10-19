package com.lunionlab.turbo_restaurant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "picture_restaurant")
@NoArgsConstructor
@Getter
@Setter
public class PictureRestaurantModel extends BaseModel {
    private String pictureUrl;
    @ManyToOne
    private RestaurantModel restaurant;

    public PictureRestaurantModel(String pictureUrl, RestaurantModel restaurant) {
        this.pictureUrl = pictureUrl;
        this.restaurant = restaurant;
    }

}
