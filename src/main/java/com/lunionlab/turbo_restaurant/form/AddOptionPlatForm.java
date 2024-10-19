package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class AddOptionPlatForm {
    @NotEmpty
    private String libelle;
    @NotNull
    private Boolean isRequired;
    @NotNull
    private Integer maxSeleteted;
    @NotNull
    private UUID platId;
}
