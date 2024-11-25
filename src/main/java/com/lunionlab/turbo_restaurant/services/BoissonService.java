package com.lunionlab.turbo_restaurant.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.form.CreateBoissonForm;
import com.lunionlab.turbo_restaurant.form.UpdateBoissonForm;
import com.lunionlab.turbo_restaurant.model.BoissonModel;
import com.lunionlab.turbo_restaurant.model.BoissonPlatModel;
import com.lunionlab.turbo_restaurant.model.PlatModel;
import com.lunionlab.turbo_restaurant.model.RestaurantModel;
import com.lunionlab.turbo_restaurant.repository.BoissonPlatRepository;
import com.lunionlab.turbo_restaurant.repository.BoissonRespository;
import com.lunionlab.turbo_restaurant.repository.PlatRepository;
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
        Optional<PlatModel> platOpt = platRepository.findFirstByIdAndDeletedAndDisponibleTrue(form.getPlatId(),
                DeletionEnum.NO);
        if (platOpt.isEmpty()) {
            log.error("plat not found");
            return ResponseEntity.badRequest().body(Report.message("message", "plat not found"));
        }
        PlatModel plat = platOpt.get();
        BoissonModel boisson = new BoissonModel(form.getLibelle(), form.getPrice(), form.getVolume());
        boisson = boissonRespository.save(boisson);
        BoissonPlatModel boissonPlat = new BoissonPlatModel(plat, boisson);
        boissonPlat = boissonPlatRepository.save(boissonPlat);

        log.info("boisson added");
        return ResponseEntity.ok(boisson);
    }

    public Object getBoissons() {
        List<BoissonModel> boissons = boissonRespository.findAllByDeleted(DeletionEnum.NO);
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
}
