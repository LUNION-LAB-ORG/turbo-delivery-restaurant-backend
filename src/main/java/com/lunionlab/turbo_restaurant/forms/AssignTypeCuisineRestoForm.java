package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import java.util.List;
import jakarta.validation.constraints.NotEmpty;

@Data
public class AssignTypeCuisineRestoForm {
    @NotEmpty
    private List<String> libelle;
}
