package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterSecondStepForm {

    @NotEmpty(message = "Le code est obligatoire !")
    private String code;
}
