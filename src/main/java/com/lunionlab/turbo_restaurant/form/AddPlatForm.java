package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
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
}
