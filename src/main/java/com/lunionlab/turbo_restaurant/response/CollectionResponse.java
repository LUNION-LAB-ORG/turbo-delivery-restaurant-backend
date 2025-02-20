package com.lunionlab.turbo_restaurant.response;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectionResponse {
    private String id;
    private Integer status;
    private Boolean deleted;
    private Date dateCreation;
    private Date dateEdition;
    private String libelle;
    private String description;
    private String picture;
    private String pictureUrl;
}
