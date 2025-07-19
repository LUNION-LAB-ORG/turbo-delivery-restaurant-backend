package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
