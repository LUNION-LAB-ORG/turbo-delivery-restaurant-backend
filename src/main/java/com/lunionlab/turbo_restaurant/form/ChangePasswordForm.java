package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordForm {
    @NotEmpty(message = "Le login est obligatoire !")
    private String username;
    @NotEmpty(message = "L'ancien mot de passe est obligatoire !")
    private String oldPassword;
    @NotEmpty(message = "Le nouveau mot de passe est obligatoire !")
    private String newPassword;
}
