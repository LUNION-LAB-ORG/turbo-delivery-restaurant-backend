package com.lunionlab.turbo_restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lunionlab.turbo_restaurant.services.PictureRestaurantService;

@RestController
@RequestMapping(path = "api/V1/turbo/resto/picture")
public class PictureRestoController {

    @Autowired
    PictureRestaurantService pictureRestaurantService;

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @PostMapping("/upload")
    public Object addRestoPicture(@PathVariable MultipartFile[] pictures) {
        return pictureRestaurantService.addRestoPicture(pictures);
    }

}
