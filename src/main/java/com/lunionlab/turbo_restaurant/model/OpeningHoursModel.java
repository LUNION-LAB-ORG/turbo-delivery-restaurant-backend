package com.lunionlab.turbo_restaurant.model;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lunionlab.turbo_restaurant.Enums.ClosedEnums;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "openingHour")
@NoArgsConstructor
@Getter
@Setter
public class OpeningHoursModel extends BaseModel {
    @ManyToOne
    @JsonBackReference
    private RestaurantModel restaurant;
    private String dayOfWeek;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Boolean closed = ClosedEnums.NO;

    public OpeningHoursModel(RestaurantModel restaurant, String dayOfWeek, LocalTime openingTime, LocalTime closingTime,
            Boolean closed) {
        this.restaurant = restaurant;
        this.dayOfWeek = dayOfWeek;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.closed = closed;
    }

}
