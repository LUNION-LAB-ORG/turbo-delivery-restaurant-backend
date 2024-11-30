package com.lunionlab.turbo_restaurant.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressRepose {
    public String libelle;
    public String etage;
    public String numeroPorte;
    public String infoSupl;
    public Object batName;
}
