package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

@Data
public class AddOptionValeurForm {
    @NotEmpty
    private String valeur;
    @NotNull
    private Long prixSup;

    // @NotNull
    private UUID optionId;
}
