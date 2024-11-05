package com.lunionlab.turbo_restaurant.controller;

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

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @GetMapping("/get")
    public Object geCollection() {
        return collectionService.getCollections();
    }

}
