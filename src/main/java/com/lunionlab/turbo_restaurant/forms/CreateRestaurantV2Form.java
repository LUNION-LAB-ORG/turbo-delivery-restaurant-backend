package com.lunionlab.turbo_restaurant.forms;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRestaurantV2Form {

    // ── Informations générales ──────────────────────────
    @NotEmpty(message = "Le nom de l'établissement est requis")
    private String nomEtablissement;

    @NotEmpty(message = "Le téléphone est requis")
    private String telephone;

    private String email;
    private String localisation;
    private String commune;
    private String codePostal;
    private String siteWeb;
    private String description;

    // ── Commission ──────────────────────────────────────
    private String typeCommission;
    private Double commission;
    private String methodRecouvrement;

    // ── Fichiers ─────────────────────────────────────────
    private String documentType;

    public void setEmail(String email) {
        this.email = email != null ? email.trim() : null;
    }
}
