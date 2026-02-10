package com.lunionlab.turbo_restaurant.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "type_cuisine_restaurant")
@NoArgsConstructor
public class TypeCuisineRestaurantModel extends BaseModel {
    @ManyToOne
    private TypeCuisineModel typeCuisine;
    @ManyToOne
    private RestaurantModel restaurant;

    public TypeCuisineRestaurantModel(TypeCuisineModel typeCuisineModel, RestaurantModel restaurantModel) {
        this.typeCuisine = typeCuisineModel;
        this.restaurant = restaurantModel;
    }

}
