package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCollectionForm {
    @NotEmpty
    private String libelle;
    @NotEmpty
    private String description;
}
