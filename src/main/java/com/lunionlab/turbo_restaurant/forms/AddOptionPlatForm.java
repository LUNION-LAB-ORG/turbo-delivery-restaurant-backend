package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
public class AddOptionPlatForm {
    @NotEmpty
    private String libelle;
    @NotNull
    private Boolean isRequired;
    @NotNull
    private Integer maxSeleteted;
    
    // @NotNull
    private UUID platId;

    // Liste des valeurs de l’option
    @Valid
    private List<AddOptionValeurForm> valeurs;
}
