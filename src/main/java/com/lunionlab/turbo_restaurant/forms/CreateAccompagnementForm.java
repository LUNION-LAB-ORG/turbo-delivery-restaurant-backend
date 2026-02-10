package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

@Data
public class CreateAccompagnementForm {
    @NotEmpty
    private String libelle;
    @NotNull
    private Long price;
    // @NotNull
    private UUID platId;
}
