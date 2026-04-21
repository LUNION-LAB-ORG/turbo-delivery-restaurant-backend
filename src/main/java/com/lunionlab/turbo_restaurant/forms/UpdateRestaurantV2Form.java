package com.lunionlab.turbo_restaurant.forms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRestaurantV2Form {

    // ── Informations générales ──────────────────────────
    private String nomEtablissement;
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
