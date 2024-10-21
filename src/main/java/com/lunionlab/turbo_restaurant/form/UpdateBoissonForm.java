package com.lunionlab.turbo_restaurant.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBoissonForm {
    private String libelle;
    private Long price;
    private Double volume;
}
