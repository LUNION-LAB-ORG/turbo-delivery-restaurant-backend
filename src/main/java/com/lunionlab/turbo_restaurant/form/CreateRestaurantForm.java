package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateRestaurantForm {
    @NotEmpty
    private String nomEtablissement;
    @NotEmpty
    private String description;
    @NotEmpty
    private String email;
    @NotEmpty
    private String telephone;
    @NotEmpty
    private String codePostal;
    @NotEmpty
    private String commune;
    @NotEmpty
    private String localisation;

    private String siteWeb;
    @NotEmpty
    private String dateService;

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

    @NotEmpty
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

    public void setLongitude(String longitude) {
        this.longitude = Double.parseDouble(longitude);
    }

    public void setLatitude(String latitude) {
        this.latitude = Double.parseDouble(latitude);
    }

    public void setIdLocation(String idLocation) {
        this.idLocation = idLocation;
    }

}
