package com.lunionlab.turbo_restaurant.response;

import java.util.List;

import com.lunionlab.turbo_restaurant.dto.CollectionSearchDto;
import com.lunionlab.turbo_restaurant.dto.PlatSearchDto;
import com.lunionlab.turbo_restaurant.dto.RestaurantSearchDto;

import lombok.Data;

@Data
public class SearchGlobalResponse {
    
    private String query;
    private List<CollectionSearchDto> tags;
    private int count;
    private List<RestaurantSearchDto> restaurants;
    private List<PlatSearchDto> plats;
}
