package com.lunionlab.turbo_restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lunionlab.turbo_restaurant.form.ChangePasswordForm;
import com.lunionlab.turbo_restaurant.form.LoginForm;
import com.lunionlab.turbo_restaurant.form.RegisterFirstStepForm;
import com.lunionlab.turbo_restaurant.form.RegisterSecondStepForm;
import com.lunionlab.turbo_restaurant.form.RegisterThirdStepForm;
import com.lunionlab.turbo_restaurant.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/V1/turbo/resto/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public Object authentication(@Valid @RequestBody LoginForm form, BindingResult result) {
        return userService.login(form, result);
    }

    @PostMapping("/register/stepfirst")
    public Object registerFirstStep(@Valid @RequestBody RegisterFirstStepForm form, BindingResult result) {
        return userService.registerFirstStep(form, result);
    }

    @PostMapping("/register/stepsecond")
    public Object registerSecondStep(@Valid @RequestBody RegisterSecondStepForm form, BindingResult result) {
        return userService.registerSecondeStep(form, result);
    }

    @PostMapping("/register/finalstep")
    public Object registerThirdStep(@Valid @RequestBody RegisterThirdStepForm form, BindingResult result) {
        return userService.registerThirdStep(form, result);
    }

    @PostMapping("/change/password")
    public Object changeMyPassword(@Valid @RequestBody ChangePasswordForm form, BindingResult result) {
        return userService.changeMyPassword(form, result);
    }

}
