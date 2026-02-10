package com.lunionlab.turbo_restaurant.entities;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "roles")
@NoArgsConstructor
public class RoleModel extends BaseModel {
    private String libelle;

    public RoleModel(String libelle) {
        this.libelle = libelle;
    }
}
