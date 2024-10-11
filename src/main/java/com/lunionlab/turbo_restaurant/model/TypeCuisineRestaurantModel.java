package com.lunionlab.turbo_restaurant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "type_cuisine_restaurant")
@NoArgsConstructor
@Getter
@Setter
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
