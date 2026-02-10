package com.lunionlab.turbo_restaurant.entities;


import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
@Table(name = "orderItem")
@NoArgsConstructor
public class OrderItemModel extends BaseModel {
    private Integer price;
    private Integer quantity;
    @ManyToOne
    private PlatModel plat;
    @ManyToOne
    @JsonBackReference
    private UserOrderM userOrderM;
    @ManyToOne
    private OptionValeurModel optionValueM;
    @ManyToOne
    private AccompagnementModel accompagnementM;
    @ManyToOne
    private BoissonModel boissonM;

    public OrderItemModel(Integer price, Integer quantity, PlatModel plat, UserOrderM userOrderM,
            OptionValeurModel optionValueM, AccompagnementModel accompagnementM, BoissonModel boissonM) {
        this.price = price;
        this.quantity = quantity;
        this.plat = plat;
        this.userOrderM = userOrderM;
        this.optionValueM = optionValueM;
        this.accompagnementM = accompagnementM;
        this.boissonM = boissonM;
    }

}
