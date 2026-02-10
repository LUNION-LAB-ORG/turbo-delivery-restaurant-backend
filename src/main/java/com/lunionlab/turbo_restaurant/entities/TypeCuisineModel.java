package com.lunionlab.turbo_restaurant.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "type_cuisine")
@NoArgsConstructor
public class TypeCuisineModel extends BaseModel {
    private String libelle;

    public TypeCuisineModel(String libelle) {
        this.libelle = libelle;
    }
}
