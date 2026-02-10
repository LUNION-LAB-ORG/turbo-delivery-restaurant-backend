package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;

@Data
public class LoginForm {
    @NotEmpty(message = "Le login est obligatoire !")
    private String username;
    @NotEmpty(message = "Le mot de passe est obligatoire !")
    private String password;
}
