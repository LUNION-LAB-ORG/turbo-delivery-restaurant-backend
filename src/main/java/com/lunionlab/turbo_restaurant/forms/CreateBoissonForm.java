package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

@Data
public class CreateBoissonForm {
    @NotEmpty
    private String libelle;
    @NotNull
    private Long price;
    @NotNull
    private Double volume;
}
