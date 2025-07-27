package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {

    @NotEmpty(message = "Le login est obligatoire !")
    private String username;
    @NotEmpty(message = "Le mot de passe est obligatoire !")
    private String password;
}
