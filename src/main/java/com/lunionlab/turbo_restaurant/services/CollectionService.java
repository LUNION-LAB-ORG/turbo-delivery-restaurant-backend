package com.lunionlab.turbo_restaurant.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.lunionlab.turbo_restaurant.Enums.CollectionEnum;
import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.form.AddCollectionForm;
import com.lunionlab.turbo_restaurant.form.UpdateCollectionForm;
import com.lunionlab.turbo_restaurant.model.CollectionModel;
import com.lunionlab.turbo_restaurant.repository.CollectionRepository;
import com.lunionlab.turbo_restaurant.utilities.Report;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CollectionService {
    @Autowired
    CollectionRepository collectionRepository;

    @Autowired
    GenericService genericService;

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

    public Object addCollection(MultipartFile picture, @Valid AddCollectionForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais format de données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }
        Boolean isExist = collectionRepository.existsByLibelleAndDeleted(form.getLibelle(), DeletionEnum.NO);
        if (isExist) {
            log.error("la collection existe deja");
            return ResponseEntity.badRequest()
                    .body(Report.getMessage("message", "cette collection existe déjà", "code", "C10"));
        }
        String pictureName = null;
        if (picture.isEmpty() || picture == null) {
            log.error("picture is required");
            return ResponseEntity.badRequest()
                    .body(Report.message("picture", "vous devez soumettre une fichier image"));
        }

        if (!picture.isEmpty() && picture != null) {
            String pictureExt = genericService.getFileExtension(picture.getOriginalFilename());
            if (!pictureExt.equalsIgnoreCase("png") && !pictureExt.equalsIgnoreCase("jpg")) {
                log.error("mauvais format de fichier");
                return ResponseEntity.badRequest()
                        .body(Report.message("picture", "l'image doit etre au format png ou jpg"));
            }
            try {
                pictureName = genericService.generateFileName("collection");
                File pictureFile = new File(pictureName + "." + pictureExt);
                picture.transferTo(pictureFile.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        CollectionModel collectionModel = new CollectionModel(form.getLibelle(), form.getDescription(), pictureName,
                pictureName);
        collectionModel = collectionRepository.save(collectionModel);

        return ResponseEntity.ok(collectionModel);
    }

    public Object detailCollection(UUID collectionId) {
        Optional<CollectionModel> collection = collectionRepository.findFirstByIdAndDeleted(collectionId,
                DeletionEnum.NO);
        if (collection.isEmpty()) {
            log.error("collection not found");
            return Report.notFound("collection not found");
        }
        return ResponseEntity.ok(collection.get());
    }

    public Object UpdateCollection(UUID collectionId, UpdateCollectionForm form, MultipartFile picture) {
        Optional<CollectionModel> collection = collectionRepository.findFirstByIdAndDeleted(collectionId,
                DeletionEnum.NO);
        if (collection.isEmpty()) {
            log.error("collection not found");
            return Report.notFound("collection not found");
        }
        CollectionModel collectionModel = collection.get();
        if (!picture.isEmpty() && picture != null) {
            String pictureExt = genericService.getFileExtension(picture.getOriginalFilename());
            if (!pictureExt.equalsIgnoreCase("png") && !pictureExt.equalsIgnoreCase("jpg")) {
                log.error("mauvais format de fichier");
                return ResponseEntity.badRequest()
                        .body(Report.message("picture", "l'image doit etre au format png ou jpg"));
            }
            try {
                String pictureName = genericService.generateFileName("collection");
                File pictureFile = new File(pictureName + "." + pictureExt);
                picture.transferTo(pictureFile.toPath());
                collectionModel.setPicture(pictureName);
                collectionModel.setPictureUrl(pictureName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!form.getLibelle().isEmpty() && form.getLibelle() != null) {
            collectionModel.setLibelle(form.getLibelle());
        }
        if (!form.getDescription().isEmpty() && form.getDescription() != null) {
            collectionModel.setDescription(form.getDescription());
        }

        collectionModel = collectionRepository.save(collectionModel);

        return ResponseEntity.ok(collectionModel);
    }

}
