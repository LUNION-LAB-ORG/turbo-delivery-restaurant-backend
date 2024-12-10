package com.lunionlab.turbo_restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lunionlab.turbo_restaurant.form.ChangePasswordForm;
import com.lunionlab.turbo_restaurant.form.LoginForm;
import com.lunionlab.turbo_restaurant.form.NewPasswordForm;
import com.lunionlab.turbo_restaurant.form.RegisterFirstStepForm;
import com.lunionlab.turbo_restaurant.form.RegisterSecondStepForm;
import com.lunionlab.turbo_restaurant.form.RegisterThirdStepForm;
import com.lunionlab.turbo_restaurant.form.UpdateProfileForm;
import com.lunionlab.turbo_restaurant.services.RoleService;
import com.lunionlab.turbo_restaurant.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/V1/turbo/resto/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

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

    @PostMapping("/forget/password")
    public Object forgetPassword(@Valid @RequestBody RegisterFirstStepForm form, BindingResult result) {
        return userService.forgetPassword(form, result);
    }

    @PostMapping("/new/password")
    public Object newPassword(@Valid @RequestBody NewPasswordForm form, BindingResult result) {
        return userService.newPassword(form, result);
    }

    @GetMapping("/profile")
    public Object profile() {
        return userService.profile();
    }

    @GetMapping("/roles")
    public Object getRoles() {
        return roleService.getRoles();
    }

    @PostMapping("/update/profile")
    public Object updateProfile(@PathVariable MultipartFile avatar, @Valid UpdateProfileForm form) {
        return userService.updateProfile(avatar, form);
    }

}
