package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;

@Data
public class NewPasswordForm {
    @NotEmpty
    private String token;
    @NotEmpty
    private String newPassword;
}
