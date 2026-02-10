package com.lunionlab.turbo_restaurant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lunionlab.turbo_restaurant.services.CollectionService;

@RestController
@RequestMapping(path = "api/turbo/resto/collection")
public class CollectionController {

    @Autowired
    CollectionService collectionService;

    @Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_USER" })
    @GetMapping("/get")
    public Object geCollection() {
        return collectionService.getCollections();
    }

    @GetMapping("/get/by/customer")
    public Object getCollectionByCustomer() {
        return collectionService.getCollectionByCustomer();
    }

}
