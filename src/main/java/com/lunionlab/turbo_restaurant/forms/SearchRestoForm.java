package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;

@Data
public class SearchRestoForm {
    @NotEmpty
    private String libelle;
}
