package com.lunionlab.turbo_restaurant.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lunionlab.turbo_restaurant.form.AddCollectionForm;
import com.lunionlab.turbo_restaurant.form.UpdateCollectionForm;
import com.lunionlab.turbo_restaurant.services.CollectionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/turbo/resto/collection")
public class CollectionController {

    @Autowired
    CollectionService collectionService;

    @GetMapping("/get")
    public Object geCollection() {
        return collectionService.getCollections();
    }

    @PostMapping("/add")
    public Object addCollection(@PathVariable MultipartFile picture, @Valid AddCollectionForm form,
            BindingResult result) {
        return collectionService.addCollection(picture, form, result);
    }

    @GetMapping("/detail/{collectionId}")
    public Object detailCollection(@PathVariable UUID collectionId) {
        return collectionService.detailCollection(collectionId);
    }

    @PostMapping("/update/{collectionId}")
    public Object updateCollection(@PathVariable UUID collectionId,
            @PathVariable(required = false) MultipartFile picture, UpdateCollectionForm form) {
        return collectionService.UpdateCollection(collectionId, form, picture);
    }

}
