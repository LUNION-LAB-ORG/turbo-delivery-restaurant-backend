package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;

@Data
public class RegisterSecondStepForm {
    @NotEmpty(message = "Le code est obligatoire !")
    private String code;
}
