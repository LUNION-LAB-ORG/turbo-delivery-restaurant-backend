package com.lunionlab.turbo_restaurant.form;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccompagnementForm {
    @NotEmpty
    private String libelle;
    @NotNull
    private Long price;
    @NotNull
    private UUID platId;
}
