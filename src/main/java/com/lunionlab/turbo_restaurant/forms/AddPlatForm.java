package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class AddPlatForm {  
    @NotEmpty
    private String libelle;
    @NotEmpty
    @Size(max = 255, message = "la description doit pas dépasser 255 caractères")
    private String description;
    @NotEmpty
    private String cookTime;
    @NotNull
    private Long price;
    @NotNull
    private UUID collectionId;

    // Liste des options du plat
    @Valid
    private List<AddOptionPlatForm> options;

    // ACCOMPAGNEMENTS
    @Valid
    private List<CreateAccompagnementForm> accompagnements;
}
