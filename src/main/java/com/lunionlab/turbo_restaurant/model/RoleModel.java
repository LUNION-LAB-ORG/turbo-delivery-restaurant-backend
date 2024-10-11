package com.lunionlab.turbo_restaurant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Getter
@Setter
public class RoleModel extends BaseModel {
    private String libelle;

    public RoleModel(String libelle) {
        this.libelle = libelle;
    }
}
