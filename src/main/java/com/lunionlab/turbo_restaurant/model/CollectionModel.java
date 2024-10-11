package com.lunionlab.turbo_restaurant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "collection")
@NoArgsConstructor
@Getter
@Setter
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
