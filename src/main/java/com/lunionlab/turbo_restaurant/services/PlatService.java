package com.lunionlab.turbo_restaurant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.Enums.StatusEnum;
import com.lunionlab.turbo_restaurant.form.AddOptionPlatForm;
import com.lunionlab.turbo_restaurant.form.AddOptionValeurForm;
import com.lunionlab.turbo_restaurant.form.AddPlatForm;
import com.lunionlab.turbo_restaurant.form.SearchPlatForm;
import com.lunionlab.turbo_restaurant.form.SearchPlatRestoForm;
import com.lunionlab.turbo_restaurant.model.CollectionModel;
import com.lunionlab.turbo_restaurant.model.OptionPlatModel;
import com.lunionlab.turbo_restaurant.model.OptionValeurModel;
import com.lunionlab.turbo_restaurant.model.PlatModel;
import com.lunionlab.turbo_restaurant.model.RestaurantModel;
import com.lunionlab.turbo_restaurant.repository.CollectionRepository;
import com.lunionlab.turbo_restaurant.repository.OptionPlatRepo;
import com.lunionlab.turbo_restaurant.repository.OptionValeurRepo;
import com.lunionlab.turbo_restaurant.repository.PlatRepository;
import com.lunionlab.turbo_restaurant.repository.RestaurantRepository;
import com.lunionlab.turbo_restaurant.utilities.Report;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PlatService {
    @Autowired
    PlatRepository platRepository;

    @Autowired
    GenericService genericService;

    @Autowired
    CollectionRepository collectionRepository;

    @Autowired
    OptionPlatRepo optionPlatRepo;

    @Autowired
    OptionValeurRepo optionValeurRepo;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    PagedResourcesAssembler<PlatModel> assembler;

    public Object addPlat(MultipartFile imageUrl, @Valid AddPlatForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais format des données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }

        if (imageUrl == null || imageUrl.isEmpty()) {
            log.error("imageUrl is required");
            return ResponseEntity.badRequest().body(Report.message("message", "Veuillez soumettre l'image du plat"));
        }
        String imageName = null;
        if (!imageUrl.isEmpty() && imageUrl != null) {
            String imageExten = genericService.getFileExtension(imageUrl.getOriginalFilename());
            if (!imageExten.equalsIgnoreCase("png") && !imageExten.equalsIgnoreCase("jpg")) {
                log.error("format image invalide");
                return ResponseEntity.badRequest()
                        .body(Report.message("message", "l'image du plat doit etre au format png ou jpg"));
            }
            imageName = genericService.generateFileName("plat_image") + "." + imageExten;
            File imageFile = new File(imageName);
            // compress and save image
            genericService.compressImage(imageUrl, imageFile);
        }
        RestaurantModel restaurantModel = genericService.getAuthUser().getRestaurant();
        if (restaurantModel == null) {
            log.error("this user has not restaurant");
            return ResponseEntity.badRequest().body(Report.message("message", "restaurant not found"));
        }
        Optional<CollectionModel> collectionOpt = collectionRepository.findFirstByIdAndDeleted(form.getCollectionId(),
                DeletionEnum.NO);
        if (collectionOpt.isEmpty()) {
            log.error("collection selected not found");
            return ResponseEntity.badRequest().body(Report.message("message", "this collection selected not found"));
        }
        PlatModel platModel = new PlatModel(form.getLibelle(), form.getDescription(), form.getPrice(), imageName,
                restaurantModel, collectionOpt.get());
        platModel.setCookTime(form.getCookTime());
        platModel = platRepository.save(platModel);

        return ResponseEntity.ok(platModel);
    }

    public Object addOptionPlat(@Valid AddOptionPlatForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais format des données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }
        Optional<PlatModel> platOpt = platRepository.findFirstByIdAndDeletedAndDisponibleTrue(form.getPlatId(),
                DeletionEnum.NO);
        if (platOpt.isEmpty()) {
            log.error("plat not found");
            return ResponseEntity.badRequest().body(Report.message("message", "plat not found"));
        }
        Boolean isExist = optionPlatRepo.existsByLibelleAndPlatAndDeleted(form.getLibelle(), platOpt.get(),
                DeletionEnum.NO);
        if (isExist) {
            log.error("cette option existe déjà");
            return ResponseEntity.badRequest().body(Report.message("message", "cette option existe déjà"));
        }

        OptionPlatModel optionPlatModel = new OptionPlatModel(form.getLibelle(), form.getIsRequired(),
                form.getMaxSeleteted(), platOpt.get());
        optionPlatModel = optionPlatRepo.save(optionPlatModel);

        return ResponseEntity.ok(optionPlatModel);
    }

    public Object ListOptionPlat() {
        List<OptionPlatModel> optionPlatModel = optionPlatRepo.findAllByDeleted(DeletionEnum.NO);
        return ResponseEntity.ok(optionPlatModel);
    }

    public Object addOptionValeur(@Valid AddOptionValeurForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais format des données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }

        Optional<OptionPlatModel> optionPlat = optionPlatRepo.findFirstByIdAndDeleted(form.getOptionId(),
                DeletionEnum.NO);
        if (optionPlat.isEmpty()) {
            log.error("this option selected not found");
            return ResponseEntity.badRequest().body(Report.message("message", "optionPlat selected not found"));
        }
        Boolean isExist = optionValeurRepo.existsByValeurAndOptionPlatModelAndDeleted(form.getValeur(),
                optionPlat.get(), DeletionEnum.NO);
        if (isExist) {
            log.error("this option value is exsite");
            return ResponseEntity.badRequest().body(Report.message("message", "cette option valeur existe deja"));
        }

        OptionValeurModel optionValeur = new OptionValeurModel(form.getValeur(), form.getPrixSup(), optionPlat.get());
        optionValeur = optionValeurRepo.save(optionValeur);
        return ResponseEntity.ok(optionValeur);
    }

    // filter or search plats

    public Object searchPlat(@Valid SearchPlatForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais de données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }
        Optional<RestaurantModel> restoOpt = restaurantRepository.findFirstByIdAndLocalisationAndStatusAndDeleted(
                form.getRestoId(), form.getAddress(), StatusEnum.RESTO_VALID_BY_OPSMANAGER, DeletionEnum.NO);
        if (restoOpt.isEmpty()) {
            log.error("restaurant not found");
            return ResponseEntity.badRequest().body("Aucune donnée trouvée");
        }
        List<PlatModel> plats = new ArrayList<PlatModel>();
        Optional<CollectionModel> collectionOpt = null;
        if (form.getCollectionId() != null) {
            collectionOpt = collectionRepository.findFirstByIdAndDeleted(form.getCollectionId(), DeletionEnum.NO);
            plats = platRepository
                    .findByRestaurantAndCollectionAndPriceGreaterThanEqualAndPriceLessThanEqualAndDeletedAndDisponibleTrue(
                            restoOpt.get(), collectionOpt.get(), form.getPriceStart(),
                            form.getPriceEnd(), DeletionEnum.NO);
        } else {
            plats = platRepository
                    .findByRestaurantAndPriceGreaterThanEqualAndPriceLessThanEqualAndDeletedAndDisponibleTrue(
                            restoOpt.get(),
                            form.getPriceStart(), form.getPriceEnd(), DeletionEnum.NO);
        }

        return ResponseEntity.ok(plats);

    }

    public Object searchPlatInResto(SearchPlatRestoForm form) {
        Optional<RestaurantModel> restoOpt = restaurantRepository
                .findFirstByNomEtablissementContainingIgnoreCaseAndDeleted(form.getRestoName(), DeletionEnum.NO);
        if (restoOpt.isEmpty()) {
            log.error("data not found");
            return ResponseEntity.badRequest().body("Aucune donnée trouvée");
        }
        List<PlatModel> plats = platRepository
                .findByRestaurantAndLibelleContainingIgnoreCaseAndDisponibleTrueAndDeletedFalse(restoOpt.get(),
                        form.getPlatName());
        return ResponseEntity.ok(plats);
    }

    public Object customerCheckExistingPlat(UUID platId) {
        Optional<PlatModel> platOpt = platRepository.findFirstByIdAndDeletedAndDisponibleTrue(platId, DeletionEnum.NO);
        if (platOpt.isEmpty()) {
            log.error("plat not found");
            return ResponseEntity.badRequest().body("plat Id " + platId + " est trouvable");
        }
        return ResponseEntity.ok(platOpt.get());
    }

    public Object getAllFoodPriceAsc() {
        return ResponseEntity.ok(platRepository.findAllPriceAsc());
    }

    public ResponseEntity<PagedModel<EntityModel<PlatModel>>> getAllFood() {
        Page<PlatModel> platResource = platRepository.findByDeletedFalseAndDisponibleTrue(genericService.pagination(0));
        PagedModel<EntityModel<PlatModel>> platR = assembler.toModel(platResource);
        return ResponseEntity.ok(platR);
    }
}
