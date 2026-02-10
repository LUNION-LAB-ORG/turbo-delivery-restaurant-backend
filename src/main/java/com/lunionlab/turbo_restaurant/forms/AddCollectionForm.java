package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;

@Data
public class AddCollectionForm {
    @NotEmpty
    private String libelle;
    @NotEmpty
    private String description;
}
