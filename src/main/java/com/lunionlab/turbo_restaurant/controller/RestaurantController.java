package com.lunionlab.turbo_restaurant.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lunionlab.turbo_restaurant.form.AddOpeningForm;
import com.lunionlab.turbo_restaurant.form.CreateRestaurantForm;
import com.lunionlab.turbo_restaurant.form.SearchRestoForm;
import com.lunionlab.turbo_restaurant.form.UpdateRestaurant;
import com.lunionlab.turbo_restaurant.form.UserOrderForm;
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

    @GetMapping("/not/validated/{page}")
    public Object getAllRestaurantNotValidated(@PathVariable Integer page) {
        return restaurantService.getAllRestaurantNotValidated(page);
    }

    @GetMapping("/validated/authservice/{page}")
    public Object getAllRestaurantValidByAuthService(@PathVariable Integer page) {
        return restaurantService.getAllRestaurantValidByAuthService(page);
    }

    @GetMapping("/validated/opsmanager/{page}")
    public Object getAllRestaurantValidByOpsManager(@PathVariable Integer page) {
        return restaurantService.getAllRestaurantValidByOpsManager(page);
    }

    @GetMapping("/approved/authservice/{restoId}")
    public Object restaurantValidatedByAuthService(@PathVariable UUID restoId) {
        return restaurantService.restaurantValidatedByAuthService(restoId);
    }

    @GetMapping("/approved/opsmanager/{restoId}")
    public Object restaurantValidatedByOpsManager(@PathVariable UUID restoId) {
        return restaurantService.restaurantValidatedByOpsManager(restoId);
    }

    @GetMapping("/detail/erp/{restoId}")
    public Object restaurantDetail(@PathVariable UUID restoId) {
        return restaurantService.restaurantDetail(restoId);
    }

    @PostMapping("/search")
    public Object searResto(@Valid @RequestBody SearchRestoForm form, BindingResult result) {
        return restaurantService.searResto(form, result);
    }

    // opening hours

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @PostMapping("/add/horaire")
    public Object addOpeningHour(@Valid @RequestBody AddOpeningForm form, BindingResult result) {
        return restaurantService.addOpeningHours(form, result);
    }

    @GetMapping("/check/opening/{restoId}")
    public ResponseEntity<Boolean> restoIspOpening(@PathVariable UUID restoId) {
        return restaurantService.restoIsOpen(restoId);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @GetMapping("/get/hours")
    public Object getOpeningHours() {
        return restaurantService.getOpeningHours();
    }

    // save user orders
    @PostMapping("/save/order")
    public ResponseEntity<Boolean> saveOrder(@RequestBody UserOrderForm form) {
        return restaurantService.saveUserOrder(form);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @GetMapping("/get/user/orders")
    public ResponseEntity<?> getUserOrders() {
        return restaurantService.getUserOrders();
    }
}
