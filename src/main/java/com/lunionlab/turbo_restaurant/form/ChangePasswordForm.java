package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class ChangePasswordForm {
    @NotEmpty
    private String username;
    @NotEmpty
    private String oldPassword;
    @NotEmpty
    private String newPassword;

    public void setUsername(String username) {
        this.username = username.toUpperCase();
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
