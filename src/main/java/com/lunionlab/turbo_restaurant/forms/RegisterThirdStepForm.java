package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;

@Data
public class RegisterThirdStepForm {
    @NotEmpty(message = "L'email est obligatoire !")
    private String email;
    @NotEmpty(message = "Le nom est obligatoire !")
    private String firstName;
    @NotEmpty(message = "Le prénom est obligatoire !")
    private String lastName;
    @NotEmpty(message = "Le téléphone est obligatoire !")
    private String telephone;
    @NotEmpty(message = "Le login est obligatoire !")
    private String username;
}
