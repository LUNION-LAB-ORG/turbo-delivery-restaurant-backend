package com.lunionlab.turbo_restaurant.controller;

import com.lunionlab.turbo_restaurant.form.*;
import com.lunionlab.turbo_restaurant.services.RoleService;
import com.lunionlab.turbo_restaurant.services.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "api/V1/turbo/resto/user")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/login")
    public Object authentication(@Valid @RequestBody LoginForm form) {
        return userService.login(form);
    }

    @PostMapping("/register/stepfirst")
    public Object registerFirstStep(@Valid @RequestBody RegisterFirstStepForm form) {
        return userService.registerFirstStep(form);
    }

    @PostMapping("/register/stepsecond")
    public Object registerSecondStep(@Valid @RequestBody RegisterSecondStepForm form) {
        return userService.registerSecondeStep(form);
    }

    @PostMapping("/register/finalstep")
    public Object registerThirdStep(@Valid @RequestBody RegisterThirdStepForm form) {
        return userService.registerThirdStep(form);
    }

    @PostMapping("/change/password")
    public Object changeMyPassword(@Valid @RequestBody ChangePasswordForm form) {
        return userService.changeMyPassword(form);
    }

    @PostMapping("/forget/password")
    public Object forgetPassword(@Valid @RequestBody RegisterFirstStepForm form) {
        return userService.forgetPassword(form);
    }

    @PostMapping("/new/password")
    public Object newPassword(@Valid @RequestBody NewPasswordForm form) {
        return userService.newPassword(form);
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
