package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;

@Data
public class UpdateRestaurant {
    private String nomEtablissement;
    private String description;
    private String email;
    private String codePostal;
    private String commune;
    private String localisation;
    private String siteWeb;
    private String dateService;
    private Double latitude;
    private Double longitude;
    private String idLocation;
}
