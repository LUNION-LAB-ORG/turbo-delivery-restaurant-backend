package com.lunionlab.turbo_restaurant.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.form.AssignTypeCuisineRestoForm;
import com.lunionlab.turbo_restaurant.model.RestaurantModel;
import com.lunionlab.turbo_restaurant.model.TypeCuisineModel;
import com.lunionlab.turbo_restaurant.model.TypeCuisineRestaurantModel;
import com.lunionlab.turbo_restaurant.model.UserModel;
import com.lunionlab.turbo_restaurant.repository.RestaurantRepository;
import com.lunionlab.turbo_restaurant.repository.TypeCuisineRestoRepository;
import com.lunionlab.turbo_restaurant.utilities.Report;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TypeCuisineRestaurantService {
    @Autowired
    TypeCuisineService typeCuisineService;
    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    TypeCuisineRestoRepository typeCuisineRestoRepository;

    @Autowired
    GenericService genericService;

    public Object assigneTypeCuisineToResto(@Valid AssignTypeCuisineRestoForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais des données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }
        if (form.getLibelle().isEmpty() || form.getLibelle() == null) {
            log.error("aucun type de cuisine choisi");
            return ResponseEntity.badRequest()
                    .body(Report.message("message", "Veuillez choisir le type de cuisine pour votre restaurant"));
        }
        UserModel userAuth = genericService.getAuthUser();
        Optional<RestaurantModel> restaurant = restaurantRepository.findFirstByIdAndDeleted(
                userAuth.getRestaurant().getId(),
                DeletionEnum.NO);
        if (restaurant.isEmpty()) {
            log.error("restaurant not found");
            return ResponseEntity.badRequest().body(Report.message("message", "aucun restaurant trouvé"));
        }

        for (String libelle : form.getLibelle()) {
            TypeCuisineModel typeCuisine = typeCuisineService.get(libelle);
            if (typeCuisine == null) {
                continue;
            }

            TypeCuisineRestaurantModel typeCuisineRestaurantModel = new TypeCuisineRestaurantModel(typeCuisine,
                    restaurant.get());

            typeCuisineRestaurantModel = typeCuisineRestoRepository.save(typeCuisineRestaurantModel);
        }

        return ResponseEntity.ok("operation effectuée avec succès");
    }

}
