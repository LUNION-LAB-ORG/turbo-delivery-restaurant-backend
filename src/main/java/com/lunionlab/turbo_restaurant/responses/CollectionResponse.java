package com.lunionlab.turbo_restaurant.responses;

import lombok.Data;
import java.util.Date;

@Data
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
