package com.lunionlab.turbo_restaurant.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lunionlab.turbo_restaurant.model.CollectionModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlatByCollectionResponse {
    private CollectionModel collectionModel;
    private Long totalPlat;
    // private List<PlatModel> plats = new ArrayList<>();

}
