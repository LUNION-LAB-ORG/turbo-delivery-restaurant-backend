package com.lunionlab.turbo_restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lunionlab.turbo_restaurant.services.TypeCuisineService;

@RestController
@RequestMapping(path = "api/V1/turbo/resto/type/cuisine")
public class TypeCuisineController {
    @Autowired
    TypeCuisineService typeCuisineService;

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @GetMapping("/liste")
    public Object getTypeCuisine() {
        return typeCuisineService.getTypeCuisine();
    }
}
