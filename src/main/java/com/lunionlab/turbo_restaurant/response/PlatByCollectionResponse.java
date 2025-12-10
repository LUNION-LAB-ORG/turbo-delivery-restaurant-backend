package com.lunionlab.turbo_restaurant.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lunionlab.turbo_restaurant.dto.PlatWithoutRestaurantAndCollectionDTO;
import com.lunionlab.turbo_restaurant.model.CollectionModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlatByCollectionResponse {
    private CollectionModel collectionModel;
    private Long totalPlat;
    private List<PlatWithoutRestaurantAndCollectionDTO> plats = new ArrayList<>();
}
