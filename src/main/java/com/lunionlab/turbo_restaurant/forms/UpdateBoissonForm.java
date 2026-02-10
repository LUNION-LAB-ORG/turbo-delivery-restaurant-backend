package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;

@Data
public class UpdateBoissonForm {
    private String libelle;
    private Long price;
    private Double volume;
}
