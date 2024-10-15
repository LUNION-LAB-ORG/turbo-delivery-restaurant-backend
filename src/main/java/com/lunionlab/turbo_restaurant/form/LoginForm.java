package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class LoginForm {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

    public void setUsername(String username) {
        this.username = username.toUpperCase();
    }

}
