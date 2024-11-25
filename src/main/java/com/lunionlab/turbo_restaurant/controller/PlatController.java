package com.lunionlab.turbo_restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lunionlab.turbo_restaurant.form.AddOptionPlatForm;
import com.lunionlab.turbo_restaurant.form.AddOptionValeurForm;
import com.lunionlab.turbo_restaurant.form.AddPlatForm;
import com.lunionlab.turbo_restaurant.form.SearchPlatForm;
import com.lunionlab.turbo_restaurant.form.SearchPlatRestoForm;
import com.lunionlab.turbo_restaurant.services.PlatService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(path = "api/V1/turbo/resto/plat")
public class PlatController {
    @Autowired
    PlatService platService;

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @PostMapping("/add")
    public Object addPlat(@PathVariable MultipartFile imageUrl, @Valid AddPlatForm form,
            BindingResult result) {
        return platService.addPlat(imageUrl, form, result);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @PostMapping("/add/option/plat")
    public Object addOptionPlat(@Valid @RequestBody AddOptionPlatForm form, BindingResult result) {
        return platService.addOptionPlat(form, result);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @GetMapping("/list/option")
    public Object ListOptionPlat() {
        return platService.ListOptionPlat();
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @PostMapping("/add/option/value")
    public Object addOptionValeur(@Valid @RequestBody AddOptionValeurForm form, BindingResult result) {
        return platService.addOptionValeur(form, result);
    }

    @PostMapping("/filter")
    public Object searchPlat(@Valid @RequestBody SearchPlatForm form, BindingResult result) {
        return platService.searchPlat(form, result);
    }

    @PostMapping("/search")
    public Object searchPlatInResto(@RequestBody SearchPlatRestoForm form) {
        return platService.searchPlatInResto(form);
    }

}
