package com.lunionlab.turbo_restaurant.objetvaleur;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lunionlab.turbo_restaurant.model.OptionPlatModel;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OptionValueResp {
    public UUID id;
    public int status;
    public boolean deleted;
    public String valeur;
    public Long prixSup;

    public OptionValueResp(OptionPlatModel optionPlatModel){
        this.id = optionPlatModel.getId();
        this.status = optionPlatModel.getStatus();
//        this.prixSup = optionPlatModel.getOptionValeurs().
    }
}
