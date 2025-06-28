package com.lunionlab.turbo_restaurant.services;

import com.lunionlab.turbo_restaurant.model.AccompagnementPlatModel;
import com.lunionlab.turbo_restaurant.repository.AccompagnementPlatRepository;
import com.lunionlab.turbo_restaurant.repository.RestaurantRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.form.CreateAccompagnementForm;
import com.lunionlab.turbo_restaurant.form.UpdateAccompagnementForm;
import com.lunionlab.turbo_restaurant.model.AccompagnementModel;
import com.lunionlab.turbo_restaurant.model.PlatModel;
import com.lunionlab.turbo_restaurant.model.RestaurantModel;
import com.lunionlab.turbo_restaurant.repository.AccompagnementRepo;
import com.lunionlab.turbo_restaurant.repository.PlatRepository;
import com.lunionlab.turbo_restaurant.utilities.Report;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccompagnementService {
    @Autowired
    AccompagnementRepo accompagnementRepo;
    @Autowired
    PlatRepository platRepository;

    @Autowired
    GenericService genericService;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    AccompagnementPlatRepository accompagnementPlatRepository;

//    public Object createAccompagnement(@Valid CreateAccompagnementForm form, BindingResult result) {
//        if (result.hasErrors()) {
//            log.error("mauvais format des données");
//            return ResponseEntity.badRequest().body(Report.getErrors(result));
//        }
//        Optional<PlatModel> platOpt = platRepository.findFirstByIdAndDeletedAndDisponibleTrue(form.getPlatId(),
//                DeletionEnum.NO);
//        if (platOpt.isEmpty()) {
//            log.error("aucun plat trouvé");
//            return ResponseEntity.badRequest().body(Report.message("message", "aucun plat trouvé"));
//        }
//        Boolean isExist = accompagnementRepo.existsByLibelleAndPlatModelAndDeleted(form.getLibelle(), platOpt.get(),
//                DeletionEnum.NO);
//        if (isExist) {
//            log.error("cet accompagnement existe déjà");
//            return ResponseEntity.badRequest().body(Report.message("message", "cet accompagnement existe déjà"));
//        }
//        AccompagnementModel accompagnementModel = new AccompagnementModel(form.getLibelle(), form.getPrice(),
//                platOpt.get(), form.isFree());
//        accompagnementModel = accompagnementRepo.save(accompagnementModel);
//        log.info("accompagnement créé avec succès");
//        return ResponseEntity.ok(accompagnementModel);
//    }

    public Object createAccompagnement(@Valid CreateAccompagnementForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais format des données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }
        Optional<PlatModel> platOpt = platRepository.findFirstByIdAndDeletedAndDisponibleTrue(form.getPlatId(),
            DeletionEnum.NO);
        if (platOpt.isEmpty()) {
            log.error("aucun plat trouvé");
            return ResponseEntity.badRequest().body(Report.message("message", "aucun plat trouvé"));
        }
        PlatModel platModel = platOpt.get();
        RestaurantModel restaurantModel = genericService.getAuthUser().getRestaurant();
        List<AccompagnementModel> accompagnementModels = new ArrayList<>();
        if(!form.getAccompagnementItemForms().isEmpty()){
            form.getAccompagnementItemForms().forEach(commande->{
                Optional<AccompagnementModel> existeAccopagnement = this.accompagnementRepo
                    .findFirstByLibelleAndDeleted(commande.getLibelle(), DeletionEnum.NO);
                if(existeAccopagnement.isEmpty()){
                    AccompagnementModel accompagnementModel = new AccompagnementModel(
                        commande.getLibelle(), commande.getPrice(), restaurantModel, commande.isFree());
                    AccompagnementModel accompagnementSave = this.accompagnementRepo.save(accompagnementModel);
                    this.enregitrerAccopagenemtPlat(accompagnementModels, platModel,accompagnementSave);
                }else{
                    AccompagnementModel accompagnementModel = existeAccopagnement.get();
                    this.enregitrerAccopagenemtPlat(accompagnementModels, platModel,accompagnementModel);
                }
            });
        }
        log.info("accompagnements créé avec succès");
        return ResponseEntity.ok(accompagnementModels);
    }

    private void enregitrerAccopagenemtPlat(List<AccompagnementModel> accompagnementModels ,
        PlatModel platModel, AccompagnementModel accompagnementSave ){
        AccompagnementPlatModel accompagnementPlatModel = new AccompagnementPlatModel(platModel, accompagnementSave);
        boolean existsAccompagnement = this.accompagnementPlatRepository
            .existsByPlatModelIdAndAccompagnementModelId(platModel.getId(), accompagnementSave.getId());
        if(!existsAccompagnement){
            this.accompagnementPlatRepository.save(accompagnementPlatModel);
        }
        accompagnementModels.add(accompagnementSave);
    }

    public Object getAccompagnementForPlat(UUID platId) {
        RestaurantModel restaurant = genericService.getAuthUser().getRestaurant();
        if (restaurant == null) {
            log.error("restaurant not found");
            return ResponseEntity.badRequest().body(Report.notFound("restaurant not found"));
        }
        Optional<PlatModel> platOpt = platRepository.findFirstByIdAndRestaurantAndDeletedAndDisponibleTrue(platId,
                restaurant,
                DeletionEnum.NO);
        if (platOpt.isEmpty()) {
            log.error("this plat not found");
            return ResponseEntity.badRequest().body(Report.message("message", "this plat not found"));
        }
        List<AccompagnementPlatModel> accompagnements = accompagnementPlatRepository.findByPlatModelAndDeleted(platOpt.get(),
                DeletionEnum.NO);
        log.info("get accompagnement list for a plat");
        return ResponseEntity.ok(accompagnements);
    }

    public Object detailAccompagnement(UUID accompagnementId) {
        Optional<AccompagnementModel> accompagnementOpt = accompagnementRepo.findFirstByIdAndDeleted(accompagnementId,
                DeletionEnum.NO);
        if (accompagnementOpt.isEmpty()) {
            log.error("this accompagnement not found");
            return ResponseEntity.badRequest().body(Report.message("message", "this accompagnement not found"));
        }
        log.info("get accompagnement");
        return ResponseEntity.ok(accompagnementOpt.get());
    }

    public Object updateAccompagnement(UUID accompagnementId, UpdateAccompagnementForm form) {
        Optional<AccompagnementModel> accompagnementOpt = accompagnementRepo.findFirstByIdAndDeleted(accompagnementId,
                DeletionEnum.NO);
        if (accompagnementOpt.isEmpty()) {
            log.error("this accompagnement not found");
            return ResponseEntity.badRequest().body(Report.message("message", "this accompagnement not found"));
        }
        AccompagnementModel accompagnement = accompagnementOpt.get();
        if (!form.getLibelle().isEmpty() && form.getLibelle() != null) {
            accompagnement.setLibelle(form.getLibelle());
        }

        if (form.getPrice() != null) {
            accompagnement.setPrice(form.getPrice());
        }

        accompagnement = accompagnementRepo.save(accompagnement);
        log.info("update accompagnement");
        return ResponseEntity.ok(accompagnement);
    }

}
