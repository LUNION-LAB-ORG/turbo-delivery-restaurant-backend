package com.lunionlab.turbo_restaurant.controllers;

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

import com.lunionlab.turbo_restaurant.forms.CreateAccompagnementForm;
import com.lunionlab.turbo_restaurant.forms.UpdateAccompagnementForm;
import com.lunionlab.turbo_restaurant.services.AccompagnementService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/V1/turbo/resto/accompagnement")
public class AccompagnementController {

    @Autowired
    AccompagnementService accompagnementService;

    @Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_USER" })
    @PostMapping("/create")
    public Object createAccompagnement(@Valid @RequestBody CreateAccompagnementForm form, BindingResult result) {
        return accompagnementService.createAccompagnement(form, result);
    }

    @Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_USER" })
    @GetMapping("/list/{platId}")
    public Object getAccompagnementForPlat(@PathVariable UUID platId) {
        return accompagnementService.getAccompagnementForPlat(platId);
    }

    @Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_USER" })
    @GetMapping("/info/{accompagnementId}")
    public Object detailAccompagnement(@PathVariable UUID accompagnementId) {
        return accompagnementService.detailAccompagnement(accompagnementId);
    }

    @Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_USER" })
    @PostMapping("/update/{accompagnementId}")
    public Object updateAccompagnement(@PathVariable UUID accompagnementId,
            @RequestBody UpdateAccompagnementForm form) {
        return accompagnementService.updateAccompagnement(accompagnementId, form);
    }
}
