package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class AddOptionValeurForm {
    @NotEmpty
    private String valeur;
    @NotNull
    private Long prixSup;
    @NotNull
    private UUID optionId;
}
