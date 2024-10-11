package com.lunionlab.turbo_restaurant.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lunionlab.turbo_restaurant.Enums.CollectionEnum;
import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.model.CollectionModel;
import com.lunionlab.turbo_restaurant.repository.CollectionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CollectionService {
    @Autowired
    CollectionRepository collectionRepository;

    public void collectionSeed() {
        CollectionModel collection_pizza = new CollectionModel(CollectionEnum.PIZZA, "Pizza", null, null);
        CollectionModel collection_dessert = new CollectionModel(CollectionEnum.DESSERT, "Desserts", null, null);
        CollectionModel collection_alcool = new CollectionModel(CollectionEnum.ALCOOL, "Alcools", null, null);
        CollectionModel collection_blinis = new CollectionModel(CollectionEnum.BLINIS, "Blinis", null, null);
        CollectionModel collection_burger = new CollectionModel(CollectionEnum.BURGER, "Hamburger", null, null);
        CollectionModel collection_halal = new CollectionModel(CollectionEnum.HALAL, "Halal", null, null);
        CollectionModel collection_local = new CollectionModel(CollectionEnum.LOCAL, "Local", null, null);
        CollectionModel collection_pates = new CollectionModel(CollectionEnum.PATE, "Pâtes", null, null);
        CollectionModel collection_patisserie = new CollectionModel(CollectionEnum.PATISSERIE, "Pâtisseries", null,
                null);
        CollectionModel collection_petitDej = new CollectionModel(CollectionEnum.PETIT_DEJ, "Petit dejeuner", null,
                null);
        CollectionModel collection_poulet = new CollectionModel(CollectionEnum.POULET, "Poulet", null, null);
        CollectionModel collection_resto = new CollectionModel(CollectionEnum.RESTO, "Restauration", null, null);
        CollectionModel collection_sha = new CollectionModel(CollectionEnum.SHAWARMA, "Shamarma", null, null);
        CollectionModel collection_shish = new CollectionModel(CollectionEnum.SHISH_KEBAB, "Shish kebab", null, null);
        CollectionModel collection_vege = new CollectionModel(CollectionEnum.VEGETARIEN, "Végétarien", null, null);

        // save
        List<CollectionModel> collections = new ArrayList<>();
        collections.add(collection_vege);
        collections.add(collection_vege);
        collections.add(collection_pates);
        collections.add(collection_pizza);
        collections.add(collection_dessert);
        collections.add(collection_alcool);
        collections.add(collection_local);
        collections.add(collection_poulet);
        collections.add(collection_resto);
        collections.add(collection_sha);
        collections.add(collection_shish);
        collections.add(collection_burger);
        collections.add(collection_halal);
        collections.add(collection_patisserie);
        collections.add(collection_blinis);
        collections.add(collection_petitDej);

        for (CollectionModel collectionModel : collections) {
            collectionRepository.save(collectionModel);
        }
        log.info("save collections");
    }

    public CollectionModel get(String libelle) {
        return collectionRepository.findFirstByLibelleAndDeleted(libelle, DeletionEnum.NO).orElse(null);
    }

    public Object getCollections() {
        List<CollectionModel> collections = collectionRepository.findAllByDeleted(DeletionEnum.NO);
        return ResponseEntity.ok(collections);
    }
}
