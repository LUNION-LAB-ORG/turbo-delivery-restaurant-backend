package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UpdatePlatForm {
    
    @NotEmpty
    private String libelle;

    @NotEmpty
    @Size(max = 255, message = "La description doit pas dépasser 255 caractères")
    private String description;

    @NotEmpty
    private String cookTime;

    @NotNull
    private Long price;

    @NotNull
    private UUID collectionId;

    @Valid
    private List<AddOptionPlatForm> options;

    @Valid
    private List<CreateAccompagnementForm> accompagnements;
}
