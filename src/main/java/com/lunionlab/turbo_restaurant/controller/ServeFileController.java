package com.lunionlab.turbo_restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lunionlab.turbo_restaurant.services.ServeFileService;

@RestController
@RequestMapping(path = "/api/serve/file")
public class ServeFileController {
    @Autowired
    ServeFileService serveFileService;

    @GetMapping("/{folder}/{fileUrl}")
    public Object getFile(@PathVariable String folder, @PathVariable String fileUrl) {
        return serveFileService.getFile(folder, fileUrl);

    }

}
