package com.lunionlab.turbo_restaurant.model;

import com.lunionlab.turbo_restaurant.Enums.OptionRequiredEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "option_plat")
@NoArgsConstructor
@Getter
@Setter
public class OptionPlatModel extends BaseModel {
    private String libelle;
    private Boolean isRequired = OptionRequiredEnum.YES;
    private Integer maxSelection = 1;
    @ManyToOne
    private PlatModel plat;

    public OptionPlatModel(String libelle, Boolean isRequired, Integer maxSelection, PlatModel plat) {
        this.libelle = libelle;
        this.isRequired = isRequired;
        this.maxSelection = maxSelection;
        this.plat = plat;
    }

}
