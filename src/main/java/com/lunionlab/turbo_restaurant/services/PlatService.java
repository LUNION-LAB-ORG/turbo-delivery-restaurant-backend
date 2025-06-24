package com.lunionlab.turbo_restaurant.services;

import com.lunionlab.turbo_restaurant.model.BoissonPlatModel;
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
import com.lunionlab.turbo_restaurant.model.AccompagnementModel;
import com.lunionlab.turbo_restaurant.model.CollectionModel;
import com.lunionlab.turbo_restaurant.model.OptionPlatModel;
import com.lunionlab.turbo_restaurant.model.OptionValeurModel;
import com.lunionlab.turbo_restaurant.model.PlatModel;
import com.lunionlab.turbo_restaurant.model.RestaurantModel;
import com.lunionlab.turbo_restaurant.repository.AccompagnementRepo;
import com.lunionlab.turbo_restaurant.repository.BoissonPlatRepository;
import com.lunionlab.turbo_restaurant.repository.CollectionRepository;
import com.lunionlab.turbo_restaurant.repository.OptionPlatRepo;
import com.lunionlab.turbo_restaurant.repository.OptionValeurRepo;
import com.lunionlab.turbo_restaurant.repository.PlatRepository;
import com.lunionlab.turbo_restaurant.repository.RestaurantRepository;
import com.lunionlab.turbo_restaurant.response.CustomerPlatResponse;
import com.lunionlab.turbo_restaurant.response.PlatByCollectionResponse;
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

    @Autowired
    AccompagnementRepo accompagnementRepo;

    @Autowired
    BoissonPlatRepository boissonPlatRepository;

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
        Optional<OptionValeurModel> optionValeurOpt = optionValeurRepo.findFirstByValeurAndOptionPlatModelAndDeleted(
                form.getValeur(),
                optionPlat.get(), DeletionEnum.NO);
        if (optionValeurOpt.isPresent()) {
            OptionValeurModel optionValeurM = optionValeurOpt.get();
            optionValeurM.setPrixSup(form.getPrixSup());
            optionValeurM = optionValeurRepo.save(optionValeurM);
            return ResponseEntity.ok(optionValeurM);
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
        List<PlatModel> plats = new ArrayList<PlatModel>();
        Optional<RestaurantModel> restoOpt = Optional.empty();
        // search by resto
        if (form.getRestoId() != null) {
            restoOpt = restaurantRepository.findFirstByIdAndStatusAndDeletedFalse(
                    form.getRestoId(), StatusEnum.RESTO_VALID_BY_OPSMANAGER);

            if (restoOpt.isEmpty()) {
                log.error("restaurant not found");
                return ResponseEntity.badRequest().body("Aucune donnée trouvée");
            }
            plats = platRepository
                    .findByPriceGreaterThanEqualAndPriceLessThanEqualAndDeletedFalseAndDisponibleTrueAndRestaurant(
                            form.getPriceStart(), form.getPriceEnd(), restoOpt.get());

            return ResponseEntity.ok(plats);
        }

        // by address
        if (form.getAddress() != null) {
            restoOpt = restaurantRepository.findFirstByLocalisationAndStatusAndDeleted(
                    form.getAddress(), StatusEnum.RESTO_VALID_BY_OPSMANAGER, DeletionEnum.NO);

            if (restoOpt.isEmpty()) {
                log.error("restaurant not found");
                return ResponseEntity.badRequest().body("Aucune donnée trouvée");
            }
            plats = platRepository
                    .findByPriceGreaterThanEqualAndPriceLessThanEqualAndDeletedFalseAndDisponibleTrueAndRestaurant(
                            form.getPriceStart(), form.getPriceEnd(), restoOpt.get());

            return ResponseEntity.ok(plats);
        }

        Optional<CollectionModel> collectionOpt = null;
        // by collection
        if (form.getCollectionId() != null) {
            collectionOpt = collectionRepository.findFirstByIdAndDeleted(form.getCollectionId(), DeletionEnum.NO);
            if (collectionOpt.isEmpty()) {
                log.error("collection not found");
                return ResponseEntity.badRequest().body("aucune donnée trouvée");
            }
            plats = platRepository
                    .findByCollectionAndPriceGreaterThanEqualAndPriceLessThanEqualAndDeletedAndDisponibleTrue(
                            collectionOpt.get(), form.getPriceStart(),
                            form.getPriceEnd(), DeletionEnum.NO);

            return ResponseEntity.ok(plats);
        }

        // by all param
        if ((form.getAddress() != null
                && !form.getAddress().isEmpty()) && form.getCollectionId() != null
                && form.getRestoId() != null) {
            collectionOpt = collectionRepository.findFirstByIdAndDeleted(form.getCollectionId(), DeletionEnum.NO);
            if (collectionOpt.isEmpty()) {
                log.error("collection not found");
                return ResponseEntity.badRequest().body("aucune donnée trouvée");
            }
            restoOpt = restaurantRepository.findFirstByIdAndLocalisationAndStatusAndDeleted(form.getRestoId(),
                    form.getAddress(), StatusEnum.RESTO_VALID_BY_OPSMANAGER, DeletionEnum.NO);
            if (restoOpt.isEmpty()) {
                log.error("restaurant not found");
                return ResponseEntity.badRequest().body("Aucune donnée trouvée");
            }

            plats = platRepository
                    .findByPriceGreaterThanEqualAndPriceLessThanEqualAndDeletedFalseAndDisponibleTrueAndRestaurantAndCollection(
                            form.getPriceStart(), form.getPriceEnd(), restoOpt.get(), collectionOpt.get());
            log.info("get plat");
            return ResponseEntity.ok(plats);
        }

        if (form.getRestoId() != null && form.getCollectionId() != null) {
            collectionOpt = collectionRepository.findFirstByIdAndDeleted(form.getCollectionId(), DeletionEnum.NO);
            if (collectionOpt.isEmpty()) {
                log.error("collection not found");
                return ResponseEntity.badRequest().body("aucune donnée trouvée");
            }
            restoOpt = restaurantRepository.findFirstByIdAndStatusAndDeletedFalse(form.getRestoId(),
                    StatusEnum.RESTO_VALID_BY_OPSMANAGER);
            if (restoOpt.isEmpty()) {
                log.error("restaurant not found");
                return ResponseEntity.badRequest().body("Aucune donnée trouvée");
            }
            plats = platRepository
                    .findByCollectionAndPriceGreaterThanEqualAndPriceLessThanEqualAndDeletedAndDisponibleTrueAndRestaurant(
                            collectionOpt.get(), form.getPriceStart(), form.getPriceEnd(), DeletionEnum.NO,
                            restoOpt.get());
            return ResponseEntity.ok(plats);

        }

        // by price
        plats = platRepository
                .findByPriceGreaterThanEqualAndPriceLessThanEqualAndDeletedFalseAndDisponibleTrue(
                        form.getPriceStart(), form.getPriceEnd());

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
        // get accompagnements lier au plat
        List<AccompagnementModel> accompagnements = accompagnementRepo.findByPlatModelAndDeleted(platOpt.get(),
                DeletionEnum.NO);
        // get Option du plat
        List<OptionPlatModel> optionPlatModels = optionPlatRepo.findByPlatAndDeletedFalse(platOpt.get());
        // get drink link to plat
         List<BoissonPlatModel> boissonPlatModels = boissonPlatRepository.findByPlatAndDeleted(platOpt.get(), DeletionEnum.NO);
        // format response
        CustomerPlatResponse response = new CustomerPlatResponse(platOpt.get(), accompagnements, optionPlatModels, boissonPlatModels);
        return ResponseEntity.ok(response);
    }

    public Object getAllFoodPriceAsc() {
        return ResponseEntity.ok(platRepository.findAllPriceAsc());
    }

    public ResponseEntity<PagedModel<EntityModel<PlatModel>>> getAllFood() {
        Page<PlatModel> platResource = platRepository.findByDeletedFalseAndDisponibleTrue(genericService.pagination(0));
        PagedModel<EntityModel<PlatModel>> platR = assembler.toModel(platResource);
        return ResponseEntity.ok(platR);
    }

    public Object getPlatByRestaurant(UUID restoId) {
        Optional<RestaurantModel> restOpt = restaurantRepository.findFirstByIdAndStatusAndDeletedFalse(restoId,
                StatusEnum.RESTO_VALID_BY_OPSMANAGER);
        if (restOpt.isEmpty()) {
            log.error("this restaurant is not exist");
            return ResponseEntity.badRequest().body("Ce restaurant n'exite pas");
        }
        Page<PlatModel> platPage = platRepository.findByRestaurantAndDeletedFalseAndDisponibleTrue(restOpt.get(),
                genericService.pagination(0));

        return ResponseEntity.ok(assembler.toModel(platPage));
    }

    public Object getAllRestoCollection(UUID restoId) {
        List<CollectionModel> collections = new ArrayList<CollectionModel>();
        Optional<RestaurantModel> restaurant = restaurantRepository.findFirstByIdAndStatusAndDeleted(restoId,
                StatusEnum.RESTO_VALID_BY_OPSMANAGER, DeletionEnum.NO);
        if (restaurant.isEmpty()) {
            log.error("le restaurant {} speficie n'est pas", restoId);
            return ResponseEntity.badRequest().body("le restaurant speficifié n'existe pas");
        }
        List<PlatModel> plats = platRepository.findByRestaurant(restaurant.get());
        if (plats.size() == 0 || plats.isEmpty()) {
            log.error("plat resource not found");
            return ResponseEntity.badRequest().body("aucune donnée trouvée");
        }
        for (PlatModel plat : plats) {
            Optional<CollectionModel> collection = collections.stream()
                    .filter(c -> c.getId().equals(plat.getCollection().getId())).findFirst();
            if (collection.isPresent()) {
                continue;
            }

            collections.add(plat.getCollection());
        }
        return ResponseEntity.ok(collections);
    }

    public Object getPlatByCollection(UUID collectionId) {
        Optional<CollectionModel> collectionOpt = collectionRepository.findFirstByIdAndDeleted(collectionId,
                DeletionEnum.NO);
        if (collectionOpt.isEmpty()) {
            log.error("collection not found");
            return ResponseEntity.badRequest().body("le type de plat spécifié est introuvable");
        }
        RestaurantModel restaurantM = genericService.getAuthUser().getRestaurant();
        if (restaurantM == null) {
            log.error("this user has not restaurant");
            return ResponseEntity.badRequest().body("le type de plat spécifié est introuvable");
        }
        List<PlatModel> plats = platRepository.findByCollectionAndRestaurantAndDeletedFalse(collectionOpt.get(),
                restaurantM);
        log.info("get plat from collection");
        return ResponseEntity.ok(plats);
    }

    public Object platgeted() {
        RestaurantModel restaurantM = genericService.getAuthUser().getRestaurant();
        if (restaurantM == null) {
            log.error("restaurant not found");
            return ResponseEntity.badRequest().body("Vous n'avez pas de restaurant");
        }
        List<PlatByCollectionResponse> platByCollectionResponses = new ArrayList<>();
        List<CollectionModel> collectionModels = platRepository
                .findCollectionHasPlat(genericService.getAuthUser().getRestaurant());
        for (CollectionModel collectionModel : collectionModels) {
            PlatByCollectionResponse platByCollectionResponse = new PlatByCollectionResponse();
            long totalPlat = platRepository.countByCollectionAndDeletedFalse(collectionModel);
            platByCollectionResponse.setTotalPlat(totalPlat);
            platByCollectionResponse.setCollectionModel(collectionModel);
            platByCollectionResponses.add(platByCollectionResponse);
        }

        return ResponseEntity.ok(platByCollectionResponses);
    }

    public Object getPlatByCollectionForCustomer(UUID collectionId) {
        Optional<CollectionModel> collectionOpt = collectionRepository.findFirstByIdAndDeleted(collectionId,
                DeletionEnum.NO);
        if (collectionOpt.isEmpty()) {
            log.error("collection not found");
            return ResponseEntity.badRequest().body("le type de plat spécifié est introuvable");
        }
        List<PlatModel> plats = platRepository.findByCollectionAndDeletedFalseAndDisponibleTrue(collectionOpt.get());
        return ResponseEntity.ok(plats);
    }
}
