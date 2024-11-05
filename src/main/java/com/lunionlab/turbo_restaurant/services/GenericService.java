package com.lunionlab.turbo_restaurant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.model.UserModel;
import com.lunionlab.turbo_restaurant.repository.CodeOptRepository;
import com.lunionlab.turbo_restaurant.repository.UserRepository;
import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import java.util.Random;
import java.util.UUID;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Value("${paginate.size}")
    private Integer PAGE_SIZE;

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

    public boolean sendMail(String from, String to, String subject, String template) {
        Resend resend = new Resend("re_bAWMzqfR_DtHaJDyKXxuhEiFuMKsY5Xdm");
        CreateEmailOptions params = CreateEmailOptions.builder().from(from).to(to).subject(subject)
                .html(template)
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

    public String template(String title, String body) {
        return String.format(
                """
                        <!DOCTYPE html>
                        <html lang="fr">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Votre code de connexion pour TurboDelivery</title>
                            <style>
                                body {
                                    font-family: Arial, sans-serif;
                                    background-color: #fff;
                                    display: flex;
                                    justify-content: center;
                                    align-items: center;
                                    height: 100vh;
                                    margin: 0;
                                }
                                .card {
                                    background-color: white;
                                    padding: 40px;
                                    border-radius: 8px;
                                    box-shadow: 0 2px 10px rgba(255, 25, 1, 0.1);
                                    text-align: center;
                                    max-width: 400px;
                                    width: 100%%;
                                }
                                .logo {
                                    width: 50px;
                                    height: 50px;
                                    background-color: #FF1901;
                                    border-radius: 50%%;
                                    margin: 0 auto 20px;
                                    position: relative;
                                    overflow: hidden;
                                }
                                .logo::before {
                                    content: "";
                                    position: absolute;
                                    top: 10px;
                                    left: 10px;
                                    width: 30px;
                                    height: 30px;
                                    background-color: white;
                                    transform: rotate(45deg);
                                }
                                h1 {
                                    color: #333;
                                    font-size: 24px;
                                    margin-bottom: 30px;
                                }
                                .login-button {
                                    background-color: #FF1901;
                                    color: white;
                                    border: none;
                                    padding: 12px 0;
                                    width: 100%%;
                                    border-radius: 6px;
                                    font-size: 16px;
                                    cursor: pointer;
                                    margin-bottom: 20px;
                                    transition: background-color 0.3s ease;
                                    text-decoration: none;
                                    display: inline-block;
                                }
                                .login-button:hover {
                                    background-color: #E61600;
                                }
                                p {
                                    color: #666;
                                    font-size: 14px;
                                    line-height: 1.5;
                                    margin-bottom: 20px;
                                }
                                .code {
                                    background-color: #f8f8f8;
                                    padding: 10px;
                                    border-radius: 4px;
                                    font-family: monospace;
                                    font-size: 18px;
                                    color: #FF1901;
                                    border: 1px solid #FFD1CC;
                                }
                                .footer {
                                    margin-top: 40px;
                                    color: #999;
                                    font-size: 12px;
                                }
                            </style>
                        </head>
                        <body>
                            <div class="card">
                                <div class="logo"><img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTarUXLw74I_E3jlLameOwATbm6PpVOAPYo6Q&s" alt="logo" width="100%%" height="100%%"></div>
                                <h1>%s</h1>
                                    %s
                                <div class="footer">TurboDelivery</div>
                            </div>
                        </body>
                        </html>
                        """,
                title,
                body // Le code OTP ou toute autre donnée que vous passez
        );

    }

    public boolean compressImage(MultipartFile file, File output) {
        try {
            Thumbnails.of(file.getInputStream()).scale(1).outputQuality(0.5).toFile(output);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public PageRequest pagination(Integer page) {
        return PageRequest.of(page, PAGE_SIZE);
    }

    public ResponseEntity<String> httpGet(String uri) throws HttpClientErrorException {
        RestTemplate httpClient = new RestTemplate();
        ResponseEntity<String> response = httpClient.getForEntity(uri, String.class);
        return response;
    }

    public ResponseEntity<String> httpPost(String uri, Map<String, Object> data) throws HttpClientErrorException {

        RestTemplate httpClient = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> head = new HashMap<String, String>();
        head.put("Content-Type", "application/json");
        headers.setAll(head);
        HttpEntity<?> request = new HttpEntity<>(data, headers);
        ResponseEntity<String> response = httpClient.postForEntity(uri, request, String.class);
        return response;
    }

}
