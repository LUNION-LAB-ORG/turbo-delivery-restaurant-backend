package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterFirstStepForm {

    @NotEmpty(message = "L'email est obligatoire !")
    private String email;
}
