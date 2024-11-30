package com.lunionlab.turbo_restaurant.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurant")
@NoArgsConstructor
@Getter
@Setter
public class RestaurantModel extends BaseModel {
    private String nomEtablissement;
    private String description;
    private String email;
    private String telephone;
    private String codePostal;
    private String commune;
    private String localisation;
    private String siteWeb;
    private String logo;
    private String logo_Url;
    private Date dateService;
    private String documentUrl;
    private String cni;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PictureRestaurantModel> pictures = new ArrayList<PictureRestaurantModel>();
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OpeningHoursModel> openingHours = new ArrayList<OpeningHoursModel>();

    public RestaurantModel(String nomEtablissement, String description, String email, String telephone,
            String codePostal, String commune, String localisation, String siteWeb, String logo, String logo_Url,
            Date dateService, String documentUrl, String cni) {
        this.nomEtablissement = nomEtablissement;
        this.description = description;
        this.email = email;
        this.telephone = telephone;
        this.codePostal = codePostal;
        this.commune = commune;
        this.localisation = localisation;
        this.siteWeb = siteWeb;
        this.logo = logo;
        this.logo_Url = logo_Url;
        this.dateService = dateService;
        this.cni = cni;
        this.documentUrl = documentUrl;
    }

    @Override
    public String toString() {
        return "RestaurantModel [nomEtablissement=" + nomEtablissement + ", description=" + description + ", email="
                + email + ", telephone=" + telephone + ", codePostal=" + codePostal + ", commune=" + commune
                + ", localisation=" + localisation + ", siteWeb=" + siteWeb + ", logo=" + logo + ", logo_Url="
                + logo_Url + ", dateService=" + dateService + "]";
    }

}
