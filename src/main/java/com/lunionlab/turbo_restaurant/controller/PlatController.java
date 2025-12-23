package com.lunionlab.turbo_restaurant.controller;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lunionlab.turbo_restaurant.form.AddOptionPlatForm;
import com.lunionlab.turbo_restaurant.form.AddOptionValeurForm;
import com.lunionlab.turbo_restaurant.form.AddPlatForm;
import com.lunionlab.turbo_restaurant.form.SearchPlatForm;
import com.lunionlab.turbo_restaurant.form.SearchPlatRestoForm;
import com.lunionlab.turbo_restaurant.form.UpdatePlatForm;
import com.lunionlab.turbo_restaurant.model.PlatModel;
import com.lunionlab.turbo_restaurant.response.SearchGlobalResponse;
import com.lunionlab.turbo_restaurant.services.PlatService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(path = "api/V1/turbo/resto/plat")
public class PlatController {
    @Autowired
    PlatService platService;

    @Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_USER" })
    @PostMapping("/add")
    public Object addPlat(@PathVariable MultipartFile imageUrl, @Valid AddPlatForm form,
            BindingResult result) {
        return platService.addPlat(imageUrl, form, result);
    }

    @Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_USER" })
    @PutMapping("/update/{platId}")
    public Object updatePlat(
            @PathVariable UUID platId,
            @RequestPart(value = "imageUrl", required = false) MultipartFile imageUrl,
            @Valid UpdatePlatForm form,
            BindingResult result) {

        return platService.updatePlat(platId, imageUrl, form, result);
    }

    @Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_USER" })
    @PostMapping("/add/option/plat")
    public Object addOptionPlat(@Valid @RequestBody AddOptionPlatForm form, BindingResult result) {
        return platService.addOptionPlat(form, result);
    }

    @Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_USER" })
    @PostMapping("/add/option/value")
    public Object addOptionValeur(@Valid @RequestBody AddOptionValeurForm form, BindingResult result) {
        return platService.addOptionValeur(form, result);
    }

    @Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_USER" })
    @GetMapping("/list/option")
    public Object ListOptionPlat() {
        return platService.ListOptionPlat();
    }    

    @PostMapping("/filter")
    public Object searchPlat(@Valid @RequestBody SearchPlatForm form, BindingResult result) {
        return platService.searchPlat(form, result);
    }

    @GetMapping("/search")
    public ResponseEntity<SearchGlobalResponse> globalSearch(
            @RequestParam("query") String query) {
        return ResponseEntity.ok(platService.globalSearch(query));
    }

    @GetMapping("/search/suggestions")
    public ResponseEntity<Map<String, Object>> globalSuggestionSearch(
            @RequestParam("query") String query) {

        Map<String, Object> response = platService.globalSuggestionSearch(query);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public Object searchPlatInResto(@RequestBody SearchPlatRestoForm form) {
        return platService.searchPlatInResto(form);
    }

    @GetMapping("/detail/{platId}")
    public Object customerCheckExistingPlat(@PathVariable UUID platId) {
        return platService.customerCheckExistingPlat(platId);
    }

    @GetMapping("/all/price")
    public Object getAllPrice() {
        return platService.getAllFoodPriceAsc();
    }

    @GetMapping("/get/all")
    public ResponseEntity<PagedModel<EntityModel<PlatModel>>> getAllFood() {
        return platService.getAllFood();
    }

    @GetMapping("/get/by/{restoId}")
    public Object getPlatByResto(@PathVariable UUID restoId) {
        return platService.getPlatByRestaurant(restoId);
    }

    @GetMapping("/get/collection/by/{restoId}")
    public Object getAllRestoCollection(@PathVariable UUID restoId) {
        return platService.getAllRestoCollection(restoId);
    }

    @Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_USER" })
    @GetMapping("/collection/{collectionId}")
    public Object getPlatByCollection(@PathVariable UUID collectionId) {
        return platService.getPlatByCollection(collectionId);
    }

    @GetMapping("/get/by/collection")
    public Object platgeted() {
        return platService.platgeted();
    }

    @Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_USER" })
    @GetMapping("/info/{platId}")
    public Object platInfo(@PathVariable UUID platId) {
        return platService.customerCheckExistingPlat(platId);
    }

    @GetMapping("/get/by/collection/{collectionId}")
    public Object getPlatByCollectionForCustomer(@PathVariable UUID collectionId) {
        return platService.getPlatByCollectionForCustomer(collectionId);
    }
}
