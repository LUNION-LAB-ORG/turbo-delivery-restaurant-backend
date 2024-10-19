package com.lunionlab.turbo_restaurant.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lunionlab.turbo_restaurant.model.PictureRestaurantModel;
import com.lunionlab.turbo_restaurant.model.RestaurantModel;
import com.lunionlab.turbo_restaurant.repository.PictureRestoRepository;
import com.lunionlab.turbo_restaurant.utilities.Report;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PictureRestaurantService {

    @Autowired
    PictureRestoRepository pictureRestoRepository;
    @Autowired
    GenericService genericService;

    @Value("${picture.max}")
    private Integer PICTURE_MAX_UPLOAD;

    public Object addRestoPicture(MultipartFile[] pictures) {
        RestaurantModel restaurantModel = genericService.getAuthUser().getRestaurant();
        if (restaurantModel == null) {
            log.error("cet utilisateur n'a pas de restaurant");
            return ResponseEntity.badRequest().body(Report.message("message", "cet utilisateur n'a pas de restaurant"));
        }
        List<Map<String, Object>> response = new ArrayList<>();
        if (pictures.length == 0) {
            log.error("aucune image n'a été soumise");
            return ResponseEntity.badRequest().body(Report.message("message", "aucune image n'a été soumise"));
        }

        if (pictures.length > PICTURE_MAX_UPLOAD.intValue()) {
            log.error("picture max upload detected");
            return ResponseEntity.badRequest().body(Report.message("messge", "Veuillez ajouter au maximum 5 images"));
        }

        for (MultipartFile picture : pictures) {
            String pictureExtension = genericService.getFileExtension(picture.getOriginalFilename());
            if (!pictureExtension.equalsIgnoreCase("png") && !pictureExtension.equalsIgnoreCase("jpg")) {
                response.add(Map.of("error",
                        "l'image " + picture.getOriginalFilename() + " doit être au format jpg ou png"));
                continue;
            }
            String pictureName = genericService.generateFileName("picture_restaurant") + "." + pictureExtension;
            File pictureFile = new File(pictureName);
            genericService.compressImage(picture, pictureFile);
            PictureRestaurantModel pictureRestaurantModel = new PictureRestaurantModel(pictureName,
                    restaurantModel);
            pictureRestaurantModel = pictureRestoRepository.save(pictureRestaurantModel);
        }
        response.add(Map.of("response", "pictures are uploaded"));
        return ResponseEntity.ok(response);
    }
}
