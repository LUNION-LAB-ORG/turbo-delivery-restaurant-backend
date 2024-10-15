package com.lunionlab.turbo_restaurant.services;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.form.CreateRestaurantForm;
import com.lunionlab.turbo_restaurant.model.RestaurantModel;
import com.lunionlab.turbo_restaurant.model.UserModel;
import com.lunionlab.turbo_restaurant.repository.RestaurantRepository;
import com.lunionlab.turbo_restaurant.repository.UserRepository;
import com.lunionlab.turbo_restaurant.utilities.Report;
import com.lunionlab.turbo_restaurant.utilities.Utility;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RestaurantService {
    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    GenericService genericService;

    @Autowired
    RoleService roleService;

    @Autowired
    UserRepository userRepository;

    public Object createRestaurant(MultipartFile logoUrl, MultipartFile cniUrl, MultipartFile docUrl,
            @Valid CreateRestaurantForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais format des données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }
        boolean isExist = restaurantRepository.existsByNomEtablissementAndDeleted(form.getNomEtablissement(),
                DeletionEnum.NO);
        if (isExist) {
            log.error("le restaurant existe déjà");
            return ResponseEntity.badRequest().body(Report.message("message", "le restaurant existe déjà"));
        }

        if (Utility.checkEmail(form.getEmail())) {
            log.error("email invalid");
            return ResponseEntity.badRequest().body(Report.message("message", "le mail est valide"));
        }

        Map<String, String> errors = new HashMap<>();

        // verification de la nullité des fichiers
        if (logoUrl.isEmpty() || logoUrl == null) {
            errors.put("logo", "vous devez soumettre une image");
            return ResponseEntity.badRequest().body(errors);
        }
        if (cniUrl.isEmpty() || cniUrl == null) {
            errors.put("cni", "vous devez soumettre un fichier pdf");
            return ResponseEntity.badRequest().body(errors);
        }
        if (docUrl.isEmpty() || docUrl == null) {
            errors.put("documentUrl", "vous devez soumettre un fichier pdf");
            return ResponseEntity.badRequest().body(errors);
        }

        String logo = null;
        String cni = null;
        String document = null;

        if (!logoUrl.isEmpty() && logoUrl != null) {
            String logExtension = genericService.getFileExtension(logoUrl.getOriginalFilename());
            if (!logExtension.equalsIgnoreCase("png") && !logExtension.equalsIgnoreCase("jpg")) {
                log.error(logExtension);
                return ResponseEntity.badRequest()
                        .body(Report.message("logo", "le logo doit etre au format jpg ou png"));
            }
            logo = genericService.generateFileName("logo");
            File logFile = new File(logo + "." + logExtension);
            try {
                logoUrl.transferTo(logFile.toPath());
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }

        }

        if (!cniUrl.isEmpty() && cniUrl != null) {
            String cniExtension = genericService.getFileExtension(cniUrl.getOriginalFilename());
            if (!cniExtension.equalsIgnoreCase("pdf")) {
                return ResponseEntity.badRequest().body(Report.message("cni", "la cni doit etre au format pdf"));
            }
            cni = genericService.generateFileName("cni");
            File cniFile = new File(cni + "." + cniExtension);
            try {
                cniUrl.transferTo(cniFile.toPath());
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        }

        if (!docUrl.isEmpty() && docUrl != null) {
            String docExtension = genericService.getFileExtension(docUrl.getOriginalFilename());
            if (!docExtension.equalsIgnoreCase("pdf")) {
                return ResponseEntity.badRequest()
                        .body(Report.message("documentUrl", "le registre de commerce doit etre au format pdf"));
            }
            document = genericService.generateFileName("document");
            File docFile = new File(document + "." + docExtension);
            try {
                docUrl.transferTo(docFile.toPath());
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        }

        UserModel user = genericService.getAuthUser();
        if (user == null) {
            log.error("user not authenticated");
            return ResponseEntity.badRequest()
                    .body(Report.message("message", "nous ne pouvons pas donner suite à votre operation"));
        }
        // save restaurant
        RestaurantModel restaurant = new RestaurantModel(form.getNomEtablissement(), form.getDescription(),
                form.getEmail(), form.getTelephone(), form.getCodePostal(), form.getCommune(), form.getLocalisation(),
                form.getSiteWeb(), logo, logo, Utility.dateFromString(form.getDateService()), document, cni);

        restaurant = restaurantRepository.save(restaurant);

        user.setRole(roleService.getAdmin());
        user.setRestaurant(restaurant);
        user = userRepository.save(user);
        Map<String, Object> response = Map.of("restaurant", restaurant, "createdBy", user);
        return ResponseEntity.ok(response);
    }
}
