package com.lunionlab.turbo_restaurant.responses;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lunionlab.turbo_restaurant.dto.PlatWithoutRestaurantAndCollectionDTO;
import com.lunionlab.turbo_restaurant.entities.CollectionModel;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlatByCollectionResponse {
    private CollectionModel collectionModel;
    private Long totalPlat;
    private List<PlatWithoutRestaurantAndCollectionDTO> plats = new ArrayList<>();
}
