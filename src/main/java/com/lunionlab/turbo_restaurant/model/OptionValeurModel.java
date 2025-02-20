package com.lunionlab.turbo_restaurant.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "option_valeur")
@NoArgsConstructor
@Getter
@Setter
public class OptionValeurModel extends BaseModel {
    private String valeur;
    private Long prixSup;
    @ManyToOne
    @JsonBackReference
    private OptionPlatModel optionPlatModel;

    public OptionValeurModel(String valeur, Long prixSup, OptionPlatModel optionPlatModel) {
        this.valeur = valeur;
        this.prixSup = prixSup;
        this.optionPlatModel = optionPlatModel;
    }

}
