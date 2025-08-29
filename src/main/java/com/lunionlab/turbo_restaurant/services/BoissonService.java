package com.lunionlab.turbo_restaurant.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.Enums.StatusEnum;
import com.lunionlab.turbo_restaurant.dto.BoissonDTO;
import com.lunionlab.turbo_restaurant.form.CreateBoissonForm;
import com.lunionlab.turbo_restaurant.form.UpdateBoissonForm;
import com.lunionlab.turbo_restaurant.model.BoissonModel;
import com.lunionlab.turbo_restaurant.model.BoissonPlatModel;
import com.lunionlab.turbo_restaurant.model.PlatModel;
import com.lunionlab.turbo_restaurant.model.RestaurantModel;
import com.lunionlab.turbo_restaurant.repository.BoissonPlatRepository;
import com.lunionlab.turbo_restaurant.repository.BoissonRespository;
import com.lunionlab.turbo_restaurant.repository.PlatRepository;
import com.lunionlab.turbo_restaurant.repository.RestaurantRepository;
import com.lunionlab.turbo_restaurant.utilities.Report;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoissonService {
    @Autowired
    BoissonRespository boissonRespository;
    @Autowired
    PlatRepository platRepository;
    @Autowired
    BoissonPlatRepository boissonPlatRepository;
    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    GenericService genericService;

    public Object createBoisson(@Valid CreateBoissonForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais format des données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }
        Boolean isExist = boissonRespository.existsByLibelleAndVolumeAndDeleted(form.getLibelle(), form.getVolume(),
                DeletionEnum.NO);
        if (isExist) {
            log.error("this boission is already exists");
            return ResponseEntity.badRequest().body(Report.message("message", "cette boisson existe dejà"));
        }
        // Optional<PlatModel> platOpt =
        // platRepository.findFirstByIdAndDeletedAndDisponibleTrue(form.getPlatId(),
        // DeletionEnum.NO);
        RestaurantModel restaurant = genericService.getAuthUser().getRestaurant();
        // if (platOpt.isEmpty()) {
        // log.error("plat not found");
        // return ResponseEntity.badRequest().body(Report.message("message", "plat not
        // found"));
        // }
        // PlatModel plat = platOpt.get();
        if (restaurant == null) {
            log.error("restaurant not found");
            return ResponseEntity.badRequest().body(Report.notFound("restaurant not found"));

        }
        BoissonModel boisson = new BoissonModel(form.getLibelle(), form.getPrice(), form.getVolume(), restaurant);
        boisson = boissonRespository.save(boisson);
        // BoissonPlatModel boissonPlat = new BoissonPlatModel(plat, boisson);
        // boissonPlat = boissonPlatRepository.save(boissonPlat);

        log.info("boisson added");
        return ResponseEntity.ok(boisson);
    }

    public Object getBoissons() {
        RestaurantModel restaurant = genericService.getAuthUser().getRestaurant();
        if (restaurant == null) {
            log.error("restaurant not found");
            return ResponseEntity.badRequest().body(Report.notFound("restaurant not found"));
        }
        List<BoissonModel> boissons = boissonRespository.findByRestaurantAndDeletedFalse(restaurant);
        return ResponseEntity.ok(boissons);
    }

    public Object getAllBoissonForPlat(UUID platId) {
        RestaurantModel restaurant = genericService.getAuthUser().getRestaurant();
        if (restaurant == null) {
            log.error("restaurant not found");
            return ResponseEntity.badRequest().body(Report.notFound("restaurant not found"));
        }
        Optional<PlatModel> plat = platRepository.findFirstByIdAndRestaurantAndDeletedAndDisponibleTrue(platId,
                restaurant,
                DeletionEnum.NO);
        if (plat.isEmpty()) {
            log.error("plat not found");
            return ResponseEntity.badRequest().body(Report.message("message", "plat not found"));
        }
        List<BoissonPlatModel> boissonPlatModels = boissonPlatRepository.findByPlatAndDeleted(plat.get(),
                DeletionEnum.NO);
        return ResponseEntity.ok(boissonPlatModels);
    }

    public Object getBoissonsRestaurant(UUID restaurantId) {
        RestaurantModel restaurant = genericService.getAuthUser().getRestaurant();
        if (restaurant == null) {
            log.error("restaurant not found");
            return ResponseEntity.badRequest().body(Report.notFound("restaurant not found"));
        }
        
        List<BoissonDTO> boissons = boissonPlatRepository.findByRestaurantAndDeleted(restaurant, DeletionEnum.NO)
            .stream()
                .map(bp -> {
                    BoissonModel b = bp.getBoissonModel();
                    return new BoissonDTO(b.getLibelle(), b.getPrice(), b.getVolume());
                })
            .toList();

        return ResponseEntity.ok(boissons);
    }

    public Object updateBoisson(UUID boissonId, UpdateBoissonForm form) {
        Optional<BoissonModel> boissoOptional = boissonRespository.findFirstByIdAndDeleted(boissonId, DeletionEnum.NO);
        if (boissoOptional.isEmpty()) {
            log.error("this boisson not found");
            return ResponseEntity.badRequest().body(Report.message("message", "this boisson not found"));
        }
        BoissonModel boisson = boissoOptional.get();
        if (!form.getLibelle().isEmpty() && form.getLibelle() != null) {
            boisson.setLibelle(form.getLibelle());
        }
        if (form.getPrice() != null) {
            boisson.setPrice(form.getPrice());
        }
        if (form.getVolume() != null) {
            boisson.setVolume(form.getVolume());
        }

        boisson = boissonRespository.save(boisson);
        return ResponseEntity.ok(boisson);
    }

    public Object deleteBoisson(UUID boissonId) {
        Optional<BoissonModel> boissoOptional = boissonRespository.findFirstByIdAndDeleted(boissonId, DeletionEnum.NO);
        if (boissoOptional.isEmpty()) {
            log.error("this boisson not found");
            return ResponseEntity.badRequest().body(Report.message("message", "this boisson not found"));
        }
        BoissonModel boisson = boissoOptional.get();
        boisson.setDeleted(DeletionEnum.YES);

        boisson = boissonRespository.save(boisson);
        return ResponseEntity.ok(boisson);
    }

    public Object drinkInfo(UUID drinkId) {
        Optional<BoissonModel> boissonOptional = boissonRespository.findFirstByIdAndDeleted(drinkId, DeletionEnum.NO);
        if (boissonOptional.isEmpty()) {
            log.error("this boisson not found");
            return ResponseEntity.badRequest().body(Report.message("message", "this boisson not found"));
        }
        BoissonModel boisson = boissonOptional.get();
        return ResponseEntity.ok(boisson);
    }

    public Object getRestoDrink(UUID restoId) {
        Optional<RestaurantModel> restoOptional = restaurantRepository.findFirstByIdAndStatusAndDeleted(restoId,
                StatusEnum.RESTO_VALID_BY_OPSMANAGER, DeletionEnum.NO);
        if (restoOptional.isEmpty()) {
            log.error("restaurant not valid");
            return ResponseEntity.badRequest().body(Report.message("message", "restaurant not valid"));
        }
        List<BoissonModel> boissons = boissonRespository.findByRestaurantAndDeletedFalse(restoOptional.get());
        return ResponseEntity.ok(boissons);
    }
}
