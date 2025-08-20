package com.lunionlab.turbo_restaurant.form;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileForm {
    private String firstName;
    private String lastName;
    private String telephone;
    private String email;
    private UUID role;
    private String apiKey;

}
