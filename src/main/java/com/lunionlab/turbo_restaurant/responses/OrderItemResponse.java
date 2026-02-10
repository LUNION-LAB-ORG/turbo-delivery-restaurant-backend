package com.lunionlab.turbo_restaurant.responses;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemResponse {
    private Integer price;
    private Integer quantity;
    private String platId;
    private String optionId;
    private String optionValue;
    private String AccompId;
    private String drinkId;
}
