package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import java.util.UUID;

@Data
public class UpdateProfileForm {
    private String firstName;
    private String lastName;
    private String telephone;
    private String email;
    private UUID role;
    private String apiKey;
}
