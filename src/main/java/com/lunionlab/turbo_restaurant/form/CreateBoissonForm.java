package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBoissonForm {
    @NotEmpty
    private String libelle;
    @NotNull
    private Long price;
    @NotNull
    private Double volume;
}
