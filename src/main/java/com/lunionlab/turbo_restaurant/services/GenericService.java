package com.lunionlab.turbo_restaurant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.model.UserModel;
import com.lunionlab.turbo_restaurant.repository.CodeOptRepository;
import com.lunionlab.turbo_restaurant.repository.UserRepository;
import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.UUID;
import java.io.File;
import java.util.List;

@Service
@Slf4j
public class GenericService {
    @Autowired
    CodeOptRepository codeOptRepository;

    @Autowired
    UserRepository userRepository;

    @Value("${code-opt.length}")
    private Integer CODE_LENGTH;
    @Value("${doc.root}")
    private String PATH_ROOT;

    public String generateOptCode() {
        Random random = new Random();
        String code;

        while (true) {
            StringBuilder codeBuilder = new StringBuilder(); // Réinitialiser à chaque itération

            for (int i = 0; i < CODE_LENGTH; i++) {
                codeBuilder.append(random.nextInt(10)); // Ajoute des chiffres aléatoires (0-9)
            }

            code = codeBuilder.toString();

            // Vérifie si le code existe déjà
            boolean codeExists = codeOptRepository.existsByCodeAndDeleted(code, DeletionEnum.NO);
            if (!codeExists) {
                break; // Si le code n'existe pas, on sort de la boucle
            }
        }

        return code;
    }

    public boolean sendMultiMail(String from, List<String> to, String subject, Object data) {
        Resend resend = new Resend("re_123456789");
        CreateEmailOptions params = CreateEmailOptions.builder().from(from).to(to).subject(subject).html(
                "<strong>" + data + "</strong>")
                .build();
        try {
            CreateEmailResponse response = resend.emails().send(params);
            log.info("email send successfully with data{} ", response.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendMail(String from, String to, String subject, Object data) {
        Resend resend = new Resend("re_bAWMzqfR_DtHaJDyKXxuhEiFuMKsY5Xdm");
        CreateEmailOptions params = CreateEmailOptions.builder().from(from).to(to).subject(subject).html(
                "<strong>" + data + "</strong>")
                .build();
        try {
            CreateEmailResponse response = resend.emails().send(params);
            log.info("email send successfully with data{} ", response.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String generateFileName(String folder) {
        File root = new File(PATH_ROOT + File.separator + folder);
        if (!root.exists()) {
            root.mkdir();
        }
        String path = "";
        while (true) {
            path = root.getAbsolutePath() + File.separator + UUID.randomUUID().toString();
            File filePath = new File(path);
            if (filePath.exists() == false) {
                path = filePath.getAbsolutePath();
                break;
            }
        }
        return path;
    }

    public String getFileExtension(String orignalFilename) {
        int lastIndex = orignalFilename.lastIndexOf(".");
        if (lastIndex == -1) {
            return null;
        }
        return orignalFilename.substring(lastIndex + 1);
    }

    public UserModel getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findFirstByUsernameAndDeleted(username, DeletionEnum.NO).orElse(null);
    }

}
