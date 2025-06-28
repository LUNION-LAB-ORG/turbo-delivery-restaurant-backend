package com.lunionlab.turbo_restaurant.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemResponse {
    private Integer price;
    private Integer quantity;
    private String platId;
    private UUID optionId;
    private String optionValue;
    private String AccompId;
    private String drinkId;
}
