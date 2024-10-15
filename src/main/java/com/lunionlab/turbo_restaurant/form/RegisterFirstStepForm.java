package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter

public class RegisterFirstStepForm {
    @NotEmpty
    private String email;

    public void setEmail(String email) {
        this.email = email.trim();
    }

}
