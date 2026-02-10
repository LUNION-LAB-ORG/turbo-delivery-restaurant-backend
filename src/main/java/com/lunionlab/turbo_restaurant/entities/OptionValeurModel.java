package com.lunionlab.turbo_restaurant.entities;


import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
@Table(name = "option_valeur")
@NoArgsConstructor
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
