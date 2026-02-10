package com.lunionlab.turbo_restaurant.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "collection")
@NoArgsConstructor
public class CollectionModel extends BaseModel {
    private String libelle;
    private String description;
    private String picture;
    private String pictureUrl;

    public CollectionModel(String libelle, String description, String picture, String pictureUrl) {
        this.libelle = libelle;
        this.description = description;
        this.picture = picture;
        this.pictureUrl = pictureUrl;
    }
}
