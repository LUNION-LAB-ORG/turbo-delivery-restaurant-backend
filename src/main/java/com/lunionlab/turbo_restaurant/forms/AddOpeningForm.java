package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@Data
public class AddOpeningForm {
    @NotEmpty
    private String dayOfWeek;
    @NotEmpty
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "L'heure doit être au format HH:mm")
    private String openingTime;
    @NotEmpty
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "L'heure doit être au format HH:mm")
    private String closingTime;

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek.toUpperCase();
    }
}
