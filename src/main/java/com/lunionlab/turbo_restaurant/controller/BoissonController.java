package com.lunionlab.turbo_restaurant.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lunionlab.turbo_restaurant.form.CreateBoissonForm;
import com.lunionlab.turbo_restaurant.form.UpdateBoissonForm;
import com.lunionlab.turbo_restaurant.services.BoissonService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/V1/turbo/resto/boisson")
public class BoissonController {
    @Autowired
    BoissonService boissonService;

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @PostMapping("/create")
    public Object createBoisson(@Valid @RequestBody CreateBoissonForm form, BindingResult result) {
        return boissonService.createBoisson(form, result);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @GetMapping("/get")
    public Object getBoissons() {
        return boissonService.getBoissons();
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @GetMapping("/get/{restaurantid}")
    public Object getBoissonsRestaurant(@PathVariable UUID restaurantId) {
        return boissonService.getBoissonsRestaurant(restaurantId);
    }

    // @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    // @GetMapping("/get/plat/{platId}")
    // public Object getAllBoissonForPlat(@PathVariable UUID platId) {
    //     return boissonService.getAllBoissonForPlat(platId);
    // }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @PostMapping("/update/{boissonId}")
    public Object updateBoisson(@PathVariable UUID boissonId, @RequestBody UpdateBoissonForm form) {
        return boissonService.updateBoisson(boissonId, form);
    }

    @GetMapping("/get/{boissonId}")
    public Object getBoisson(@PathVariable UUID boissonId) {
        return boissonService.drinkInfo(boissonId);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @GetMapping("/info/{boissonId}")
    public Object getBoissonInfo(@PathVariable UUID boissonId) {
        return boissonService.drinkInfo(boissonId);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @GetMapping("/delete/{boissonId}")
    public Object deleteBoisson(@PathVariable UUID boissonId) {
        return boissonService.deleteBoisson(boissonId);
    }

    @GetMapping("/get/by/resto/{restoId}")
    public Object getBoissonByResto(@PathVariable UUID restoId) {
        return boissonService.getRestoDrink(restoId);
    }
}
