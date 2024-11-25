package com.lunionlab.turbo_restaurant.services;

import java.io.File;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServeFileService {
    @Value("${doc.root}")
    private String PATH_ROOT;

    public Object getFile(String folder, String fileName) {
        File file = new File(PATH_ROOT + File.separator + folder + File.separator + fileName);
        if (!file.exists()) {
            log.error("file not found");
            return ResponseEntity.badRequest().body("file not found");
        }
        if (!file.isFile()) {
            log.error("isn't a good file");
            return ResponseEntity.badRequest().body("file not found");
        }
        try {
            byte[] readFile = Files.readAllBytes(file.toPath());
            ByteArrayResource byteArrayResource = new ByteArrayResource(readFile);

            // configuration des headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(readFile.length).body(byteArrayResource);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
