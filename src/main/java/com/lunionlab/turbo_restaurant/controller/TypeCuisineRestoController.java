package com.lunionlab.turbo_restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lunionlab.turbo_restaurant.form.AssignTypeCuisineRestoForm;
import com.lunionlab.turbo_restaurant.services.TypeCuisineRestaurantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/V1/turbo/resto/typecuisine")
public class TypeCuisineRestoController {
    @Autowired
    TypeCuisineRestaurantService typeCuisineRestaurantService;

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @PostMapping("/assign")
    public Object assigneTypeCuisineToResto(@Valid @RequestBody AssignTypeCuisineRestoForm form, BindingResult result) {
        return typeCuisineRestaurantService.assigneTypeCuisineToResto(form, result);
    }
}
