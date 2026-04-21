package com.lunionlab.turbo_restaurant.forms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpeningHourEntryForm {
    private String dayOfWeek;
    private String openingTime;
    private String closingTime;
    private Boolean closed = false;

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek != null ? dayOfWeek.toUpperCase() : null;
    }
}
