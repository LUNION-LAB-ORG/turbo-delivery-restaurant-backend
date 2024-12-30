package com.lunionlab.turbo_restaurant.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "userOrder")
@NoArgsConstructor
@Getter
@Setter
public class UserOrderM extends BaseModel {
    private String orderId;
    private String recipientName;
    private String recipientPhone;
    private String paymentMethod;
    private String orderState;
    private Long totalAmount;
    private String deliveryAddress;
    private String codeOrder;
    private Long deliveryFee;
    private Long serviceFee;
    @ManyToOne
    private RestaurantModel restaurant;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userOrderM", orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItemModel> orderItemM = new ArrayList<OrderItemModel>();

    public UserOrderM(String orderId, String recipientName, String recipientPhone, String paymentMethod,
            String orderState, Long totalAmount, String deliveryAddress, RestaurantModel restaurant) {
        this.orderId = orderId;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.paymentMethod = paymentMethod;
        this.orderState = orderState;
        this.totalAmount = totalAmount;
        this.deliveryAddress = deliveryAddress;
        this.restaurant = restaurant;
    }

}
