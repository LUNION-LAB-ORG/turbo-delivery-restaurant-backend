package com.lunionlab.turbo_restaurant.form;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lunionlab.turbo_restaurant.response.AddressRepose;
import com.lunionlab.turbo_restaurant.response.OrderItemResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserOrderForm {
    private String id;
    private Long totalAmount;
    private String orderState;
    private String recipientName;
    private String recipientPhone;
    private String paymentMethod;
    private List<OrderItemResponse> orderItemM;
    private AddressRepose adresseM;
    private String restoId;

}
