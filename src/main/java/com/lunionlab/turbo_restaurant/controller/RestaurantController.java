package com.lunionlab.turbo_restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lunionlab.turbo_restaurant.form.CreateRestaurantForm;
import com.lunionlab.turbo_restaurant.form.UpdateRestaurant;
import com.lunionlab.turbo_restaurant.services.RestaurantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/V1/turbo/restaurant")
public class RestaurantController {
    @Autowired
    RestaurantService restaurantService;

    @Secured("ROLE_USER")
    @PostMapping("/create")
    public Object createRestaurant(@PathVariable MultipartFile logoUrl, @PathVariable MultipartFile cniUrl,
            @PathVariable MultipartFile docUrl, @Valid CreateRestaurantForm form, BindingResult result) {
        return restaurantService.createRestaurant(logoUrl, cniUrl, docUrl, form, result);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @PostMapping("/update")
    public Object updateRestaurant(@PathVariable(required = false) MultipartFile logoUrl,
            @PathVariable(required = false) MultipartFile cniUrl,
            @PathVariable(required = false) MultipartFile docUrl, UpdateRestaurant form) {
        return restaurantService.updateRestaurant(logoUrl, cniUrl, docUrl, form);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @GetMapping("/info")
    public Object getUserRestaurant() {
        return restaurantService.getUserAuthRestaurant();
    }
}
