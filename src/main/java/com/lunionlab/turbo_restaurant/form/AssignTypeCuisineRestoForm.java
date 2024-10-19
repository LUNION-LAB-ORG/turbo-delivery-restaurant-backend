package com.lunionlab.turbo_restaurant.form;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignTypeCuisineRestoForm {
    @NotEmpty
    private List<String> libelle;
}
