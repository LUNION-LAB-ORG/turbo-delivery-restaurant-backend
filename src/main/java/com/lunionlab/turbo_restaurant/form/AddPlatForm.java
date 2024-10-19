package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class AddPlatForm {
    @NotEmpty
    private String libelle;
    @NotEmpty
    private String description;
    @NotNull
    private Long price;
    @NotNull
    private UUID collectionId;
}
