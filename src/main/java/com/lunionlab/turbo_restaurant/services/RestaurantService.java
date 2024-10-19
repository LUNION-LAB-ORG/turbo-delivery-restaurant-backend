package com.lunionlab.turbo_restaurant.services;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.form.CreateRestaurantForm;
import com.lunionlab.turbo_restaurant.form.UpdateRestaurant;
import com.lunionlab.turbo_restaurant.model.PictureRestaurantModel;
import com.lunionlab.turbo_restaurant.model.RestaurantModel;
import com.lunionlab.turbo_restaurant.model.TypeCuisineRestaurantModel;
import com.lunionlab.turbo_restaurant.model.UserModel;
import com.lunionlab.turbo_restaurant.repository.PictureRestoRepository;
import com.lunionlab.turbo_restaurant.repository.RestaurantRepository;
import com.lunionlab.turbo_restaurant.repository.TypeCuisineRestoRepository;
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

    @Autowired
    PictureRestoRepository pictureRestoRepository;

    @Autowired
    TypeCuisineRestoRepository typeCuisineRestoRepository;

    public Object createRestaurant(MultipartFile logoUrl, MultipartFile cniUrl, MultipartFile docUrl,
            @Valid CreateRestaurantForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais format des données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }
        boolean isExist = restaurantRepository.existsByNomEtablissementAndEmailAndDeleted(form.getNomEtablissement(),
                form.getEmail(), DeletionEnum.NO);
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
            logo = genericService.generateFileName("logo") + "." + logExtension;
            File logFile = new File(logo);
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
            cni = genericService.generateFileName("cni") + "." + cniExtension;
            File cniFile = new File(cni);
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
            document = genericService.generateFileName("document") + "." + docExtension;
            File docFile = new File(document);
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

    public Object updateRestaurant(MultipartFile logoUrl, MultipartFile cniUrl, MultipartFile docUrl,
            UpdateRestaurant form) {
        UserModel userAuth = genericService.getAuthUser();
        RestaurantModel restaurant = userAuth.getRestaurant();
        if (!logoUrl.isEmpty() && logoUrl != null) {
            String logExtension = genericService.getFileExtension(logoUrl.getOriginalFilename());
            if (!logExtension.equalsIgnoreCase("png") && !logExtension.equalsIgnoreCase("jpg")) {
                log.error(logExtension);
                return ResponseEntity.badRequest()
                        .body(Report.message("logo", "le logo doit etre au format jpg ou png"));
            }
            String logo = genericService.generateFileName("logo");
            File logFile = new File(logo + "." + logExtension);
            try {
                logoUrl.transferTo(logFile.toPath());
                restaurant.setLogo(logo);
                restaurant.setLogo_Url(logo);
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        }

        if (!cniUrl.isEmpty() && cniUrl != null) {
            String cniExtension = genericService.getFileExtension(cniUrl.getOriginalFilename());
            if (!cniExtension.equalsIgnoreCase("pdf")) {
                return ResponseEntity.badRequest().body(Report.message("cni", "la cni doit etre au format pdf"));
            }
            String cni = genericService.generateFileName("cni") + "." + cniExtension;
            File cniFile = new File(cni);
            try {
                cniUrl.transferTo(cniFile.toPath());
                restaurant.setCni(cni);
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
            String document = genericService.generateFileName("document") + "." + docExtension;
            File docFile = new File(document);
            try {
                docUrl.transferTo(docFile.toPath());
                restaurant.setDocumentUrl(document);
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        }

        if (form.getCodePostal() != null && !form.getCodePostal().isEmpty()) {
            restaurant.setCodePostal(form.getCodePostal());
        }

        if (form.getCommune() != null && !form.getCommune().isEmpty()) {
            restaurant.setCommune(form.getCommune());
        }

        if (form.getDateService() != null && !form.getDateService().isEmpty()) {
            restaurant.setDateService(Utility.dateFromString(form.getDateService()));
        }

        if (form.getDescription() != null && !form.getDescription().isEmpty()) {
            restaurant.setDescription(form.getDescription());
        }

        if (form.getEmail() != null && !form.getEmail().isEmpty()) {
            restaurant.setEmail(form.getEmail());
        }
        if (form.getLocalisation() != null && !form.getLocalisation().isEmpty()) {
            restaurant.setLocalisation(form.getLocalisation());
        }
        if (form.getNomEtablissement() != null && !form.getNomEtablissement().isEmpty()) {
            restaurant.setNomEtablissement(form.getNomEtablissement());
        }

        if (form.getSiteWeb() != null && !form.getSiteWeb().isEmpty()) {
            restaurant.setSiteWeb(form.getSiteWeb());
        }

        if (form.getTelephone() != null && !form.getTelephone().isEmpty()) {
            restaurant.setTelephone(form.getTelephone());
        }

        restaurant = restaurantRepository.save(restaurant);

        return ResponseEntity.ok(restaurant);
    }

    public Object getUserAuthRestaurant() {
        UserModel user = genericService.getAuthUser();
        List<PictureRestaurantModel> pictures = new ArrayList<>();
        List<TypeCuisineRestaurantModel> typecuisines = new ArrayList<>();
        if (user.getRestaurant() == null) {
            log.error("this user hasn't any restaurant");
            return ResponseEntity.badRequest()
                    .body(Report.message("message", "Cet utilisateur n'a pas encore ajouté son restaurant"));
        }
        pictures = pictureRestoRepository.findByRestaurantAndDeleted(user.getRestaurant(), DeletionEnum.NO);
        typecuisines = typeCuisineRestoRepository.findByRestaurantAndDeleted(user.getRestaurant(), DeletionEnum.NO);
        Map<String, Object> response = new HashMap<>();
        response.put("restaurant", user.getRestaurant());
        response.put("pictures", pictures);
        response.put("typecuisine", typecuisines);
        return ResponseEntity.ok(response);
    }
}
