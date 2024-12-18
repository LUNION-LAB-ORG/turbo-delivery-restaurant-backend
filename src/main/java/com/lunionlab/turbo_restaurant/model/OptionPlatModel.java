package com.lunionlab.turbo_restaurant.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lunionlab.turbo_restaurant.Enums.OptionRequiredEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "optionPlatModel", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OptionValeurModel> optionValeurs = new ArrayList<OptionValeurModel>();

    public OptionPlatModel(String libelle, Boolean isRequired, Integer maxSelection, PlatModel plat) {
        this.libelle = libelle;
        this.isRequired = isRequired;
        this.maxSelection = maxSelection;
        this.plat = plat;
    }

}
