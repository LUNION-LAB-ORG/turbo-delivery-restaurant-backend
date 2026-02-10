package com.lunionlab.turbo_restaurant.responses;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressRepose {
    public String libelle;
    public String etage;
    public String numeroPorte;
    public String infoSupl;
    public Object batName;
}
