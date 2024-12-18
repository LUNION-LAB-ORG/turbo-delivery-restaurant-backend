package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class RegisterThirdStepForm {
    @NotEmpty
    private String email;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String telephone;

    @NotEmpty
    private String username;

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setUsername(String username) {
        this.username = username.toUpperCase().replace(" ", "").trim();
    }

}
