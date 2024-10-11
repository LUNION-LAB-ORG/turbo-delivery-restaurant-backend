package com.lunionlab.turbo_restaurant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "type_cuisine")
@NoArgsConstructor
@Getter
@Setter
public class TypeCuisineModel extends BaseModel {
    private String libelle;

    public TypeCuisineModel(String libelle) {
        this.libelle = libelle;
    }
}
