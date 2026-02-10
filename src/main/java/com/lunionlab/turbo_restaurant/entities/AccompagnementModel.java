package com.lunionlab.turbo_restaurant.entities;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "accompagnement")
@NoArgsConstructor
public class AccompagnementModel extends BaseModel {
    private String libelle;
    private Long price;
    @ManyToOne
    private PlatModel platModel;

    public AccompagnementModel(String libelle, Long price, PlatModel platModel) {
        this.libelle = libelle;
        this.price = price;
        this.platModel = platModel;
    }
}
