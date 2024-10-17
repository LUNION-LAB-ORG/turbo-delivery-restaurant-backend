package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class NewPasswordForm {
    @NotEmpty
    private String token;
    @NotEmpty
    private String newPassword;
}
