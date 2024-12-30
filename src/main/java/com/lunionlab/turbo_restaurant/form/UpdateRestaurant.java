package com.lunionlab.turbo_restaurant.form;

import lombok.Getter;

@Getter
public class UpdateRestaurant {
    private String nomEtablissement;
    private String description;
    private String email;
    private String telephone;
    private String codePostal;
    private String commune;
    private String localisation;
    private String siteWeb;
    private String dateService;
    private Double latitude;
    private Double longitude;
    private String idLocation;

    public void setNomEtablissement(String nomEtablissement) {
        this.nomEtablissement = nomEtablissement;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public void setDateService(String dateService) {
        this.dateService = dateService;
    }

    public void setLatitude(String latitude) {
        this.latitude = Double.parseDouble(latitude);
    }

    public void setLongitude(String longitude) {
        this.longitude = Double.parseDouble(longitude);
    }

    public void setIdLocation(String idLocation) {
        this.idLocation = idLocation;
    }

}
