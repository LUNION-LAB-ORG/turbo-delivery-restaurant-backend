package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;

@Data
public class RegisterFirstStepForm {
    @NotEmpty(message = "L'email est obligatoire !")
    private String email;
}
