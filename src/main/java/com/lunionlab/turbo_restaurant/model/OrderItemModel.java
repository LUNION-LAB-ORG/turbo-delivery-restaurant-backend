package com.lunionlab.turbo_restaurant.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orderItem")
@NoArgsConstructor
@Getter
@Setter
public class OrderItemModel extends BaseModel {
    private Integer price;
    private Integer quantity;
    @ManyToOne
    private PlatModel plat;
    @ManyToOne
    @JsonBackReference
    private UserOrderM userOrderM;

    public OrderItemModel(Integer price, Integer quantity, PlatModel plat, UserOrderM userOrderM) {
        this.price = price;
        this.quantity = quantity;
        this.plat = plat;
        this.userOrderM = userOrderM;
    }

}
