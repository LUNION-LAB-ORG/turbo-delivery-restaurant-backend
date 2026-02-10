package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lunionlab.turbo_restaurant.responses.AddressRepose;
import com.lunionlab.turbo_restaurant.responses.OrderItemResponse;


@Data
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
    private Long deliveryFee;
    private Long serviceFee;

}
