package com.lunionlab.turbo_restaurant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SeederService {
    @Autowired
    RoleService roleService;
    @Autowired
    TypeCuisineService typeCuisineService;

    public void run() {
        roleService.RoleSeeder();
        typeCuisineService.TypeCuisineSeeder();
    }
}
