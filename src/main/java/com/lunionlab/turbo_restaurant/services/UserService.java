package com.lunionlab.turbo_restaurant.services;

import com.lunionlab.turbo_restaurant.Enums.ChangePassword;
import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.Enums.JwtAudienceEnum;
import com.lunionlab.turbo_restaurant.Enums.StatusEnum;
import com.lunionlab.turbo_restaurant.exception.ErreurException;
import com.lunionlab.turbo_restaurant.exception.ObjetNonAuthoriseException;
import com.lunionlab.turbo_restaurant.form.*;
import com.lunionlab.turbo_restaurant.model.CodeOptModel;
import com.lunionlab.turbo_restaurant.model.RoleModel;
import com.lunionlab.turbo_restaurant.model.UserModel;
import com.lunionlab.turbo_restaurant.repository.CodeOptRepository;
import com.lunionlab.turbo_restaurant.repository.UserRepository;
import com.lunionlab.turbo_restaurant.utilities.Report;
import com.lunionlab.turbo_restaurant.utilities.Utility;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final CodeOptRepository codeOptRepository;

    private final JwtService jwtService;
    private final GenericService genericService;
    private final UserPasswordService userPasswordService;
    private final RoleService roleService;

    @Value("${password.expired-delay}")
    private Integer PASSWORD_DELAY;

    @Value("${password.length}")
    private Integer PASSWORD_LENGTH;
    @Value("${password.max-tentative}")
    private Integer MAX_ATTEMPT_CONNEXION;

    @Value("${code-opt.delay}")
    private Integer CODE_DELAY;

    @Value("${front.base_url}")
    private String FRONT_BASEURL;

    public UserService(
            UserRepository userRepository,
            CodeOptRepository codeOptRepository,
            JwtService jwtService,
            GenericService genericService,
            UserPasswordService userPasswordService,
            RoleService roleService
    ) {
        this.userRepository = userRepository;
        this.codeOptRepository = codeOptRepository;
        this.jwtService = jwtService;
        this.genericService = genericService;
        this.userPasswordService = userPasswordService;
        this.roleService = roleService;
    }

    public Object getUserByUsername(String username) {
        return userRepository.findFirstByUsernameAndDeleted(username, DeletionEnum.NO).orElse(null);

    }

    public UserModel getUserByUsernameAndStatus(String username, Integer status) {
        return userRepository.findFirstByUsernameAndStatusAndDeleted(username, status, DeletionEnum.NO).orElse(null);
    }

    public UserModel getUserByEmail(String email) {
        return userRepository.findFirstByEmailAndDeleted(email, DeletionEnum.NO).orElse(null);
    }

    public Object login(@Valid LoginForm form) {
        UserModel user = this.getUserByUsernameAndStatus(form.getUsername(), StatusEnum.DEFAULT_ENABLE);
        if (user == null) {
            log.error("user not found");
            throw new BadCredentialsException("Login ou password incorrecte");
        }
        /*
        if (user.getChangePassword() == null || user.getChangePassword() == ChangePassword.No) {
            throw new ObjetNonAuthoriseException("CHANGER_MOT_PASSE");
        }
        */
        if (user.getStatus().intValue() == StatusEnum.DEFAULT_DESABLE.intValue()
                && user.getChangePassword() == ChangePassword.YES) {
            throw new ErreurException("Compte inactif, veuillez contacter l'administrateur !");
        }

        Date now = new Date();

        if (Utility.checkPassword(form.getPassword(), user.getPassword())) {

            if (user.getExpiredPassword() == null) {
                user.setExpiredPassword(Utility.dateFromInteger(PASSWORD_DELAY, ChronoUnit.DAYS));
                user = userRepository.save(user);
            }
            /*
            if (user.getExpiredPassword().compareTo(now) < 0) {
                throw new ObjetNonAuthoriseException("MOT_PASSE_EXPIRER");
            }
            */
            // set attempt max connexion
            user.setAttempt(0);
            user = userRepository.save(user);

            // generate token
            String token = jwtService.generateToken(user.getUsername(), JwtAudienceEnum.USER);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", user);
            if (user.getRestaurant() == null) {
                response.put("is_New", true);
            } else {
                response.put("is_New", false);
            }

            // List<Integer> status = new ArrayList<>();
            // status.add(StatusEnum.RESTO_VALID_BY_AUTHSERVICE);
            // status.add(StatusEnum.RESTO_VALID_BY_OPSMANAGER);

            // if (user.getRestaurant() != null
            // && !status.contains(user.getStatus())) {
            // response.put("is_New", true);
            // }

            return ResponseEntity.ok(response);
        }

        if (!Utility.checkPassword(form.getPassword(), user.getPassword())) {
            Integer attempt = user.getAttempt() != null ? user.getAttempt().intValue() + 1 : 1;
            user.setAttempt(attempt);
            user = userRepository.save(user);
            if (user.getAttempt().intValue() >= MAX_ATTEMPT_CONNEXION.intValue()) {
                log.error("tentative de connexion atteint");
                user.setStatus(StatusEnum.DEFAULT_DESABLE);
                user = userRepository.save(user);
                throw new ErreurException("Vous avez atteint votre tentative de connexion !");
            }
        }

        throw new BadCredentialsException("Login ou password incorrecte !");
    }

    public Object registerFirstStep(@Valid RegisterFirstStepForm form) {
        String email = form.getEmail();

        if (!Utility.checkEmail(email)) {
            log.error("email invalide");
            throw new ErreurException("Email invalide !");
        }

            if (this.userRepository.existsByEmail(email)) {
                throw new ErreurException("L'email existe déjà !");
            }

        Optional<UserModel> userOpt = userRepository.findFirstByEmail(email);
        if (userOpt.isPresent()) {
            log.error("utilisateur déjè inscript");

            String codeOpt = genericService.generateOptCode();
            boolean hasSend = genericService.sendMail("support@turbodeliveryapp.com", email,
                    "Code de confirmation",
                    genericService.template("Votre code de confirmation pour TurboDelivery", "\r\n" + //
                            "                                <p>Ce code ne sera valide que pour les 5 prochaines minutes</p>\r\n"
                            + //
                            "                                <div class=\"code\">" + codeOpt + "</div>"));
            if (!hasSend) {
                log.error("email not sended");
                throw new ErreurException("Impossible d'envoyer le code de confirmation, Veuillez contacter l'administrateur !");
            }

            CodeOptModel code = new CodeOptModel(codeOpt, Utility.dateFromInteger(CODE_DELAY, ChronoUnit.MINUTES),
                    userOpt.get());
            code = codeOptRepository.save(code);
            Map<String, Object> response = Map.of("codeOpt", code.getCode(), "user", userOpt.get());
            return ResponseEntity.ok(response);
        }

        String codeOpt = genericService.generateOptCode();
        boolean hasSend = genericService.sendMail("support@turbodeliveryapp.com", email,
                "Code de confirmation",
                genericService.template("Votre code de confirmation pour TurboDelivery", "\r\n" + //
                        "                                <p>Ce code ne sera valide que pour les 5 prochaines minutes</p>\r\n"
                        + //
                        "                                <div class=\"code\">" + codeOpt + "</div>"));
        if (!hasSend) {
            log.error("email not sended");
            throw new ErreurException("Impossible d'envoyer le code de confirmation, Veuillez contacter l'administrateur !");
        }

        UserModel userModel = new UserModel(null, null, email, null, null);
        userModel = userRepository.save(userModel);
        CodeOptModel code = new CodeOptModel(codeOpt, Utility.dateFromInteger(CODE_DELAY, ChronoUnit.MINUTES),
                userModel);
        code = codeOptRepository.save(code);
        Map<String, Object> response = Map.of("codeOpt", code.getCode(), "user", userModel);
        return ResponseEntity.ok(response);

    }

    public Object registerSecondeStep(@Valid RegisterSecondStepForm form) {
        Date now = new Date();
        Optional<CodeOptModel> codeOpt = codeOptRepository.findFirstByCode(form.getCode());
        if (codeOpt.isEmpty()) {
            log.error("code not found");
            throw new ErreurException("Code invalide !");
        }

        CodeOptModel codeModel = codeOpt.get();
        if (codeModel.getExpired().compareTo(now) < 0) {
            log.error("code expiré");
            codeOptRepository.delete(codeModel);
            throw new ErreurException("Code expiré !");
        }

        codeOptRepository.delete(codeModel);

        return ResponseEntity.ok("code verification is succès !");
    }

    public Object registerThirdStep(@Valid RegisterThirdStepForm form) {
        Optional<UserModel> userOpt = userRepository.findFirstByEmail(form.getEmail());
        if (userOpt.isEmpty()) {
            log.error("user not found");
            throw new ErreurException("Ce email n'existe pas !");
        }

        String password = Utility.generatePassword(PASSWORD_LENGTH);
        String apikey = Utility.genererNouveauApiKey();
        UserModel user = userOpt.get();
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setTelephone(form.getTelephone());
        user.setUsername(form.getUsername());
        user.setPassword(Utility.hashPassword(password));
        user.setExpiredPassword(Utility.dateFromInteger(PASSWORD_DELAY, ChronoUnit.DAYS));
        user.setChangePassword(ChangePassword.No);
        user.setStatus(StatusEnum.DEFAULT_ENABLE);
        user.setApiKey(apikey);
        user = userRepository.save(user);
        Map<String, Object> response = Map.of("password", password, "user", user);
        log.info("user info save successfull");
        return ResponseEntity.ok(response);
    }

    public Object changeMyPassword(@Valid ChangePasswordForm form) {
        UserModel user = this.getUserByUsernameAndStatus(form.getUsername(), StatusEnum.DEFAULT_ENABLE);
        if (user == null) {
            log.error("user not found");
            throw new ErreurException("Login incorrecte !");
        }

        if (!Utility.checkPassword(form.getOldPassword(), user.getPassword())) {
            log.error("old password isn't trusted");
            throw new ErreurException("Ancien de mot de pass incorrecte !");
        }

        boolean isValid = Utility.passwordValidator(form.getNewPassword(), PASSWORD_LENGTH);

        if (!isValid) {
            return ResponseEntity.badRequest().body(
                    Report.getMessage("message",
                            "le mot de passe doit contenir au moins" + PASSWORD_LENGTH
                                    + " , dont  une lettre majuscule,miniscule,chiffre et un caractere special",
                            "code", "CHP13"));
        }

        boolean isExist = userPasswordService.saveUserPassword(form.getNewPassword(), user);
        if (!isExist) {
            log.error("cet mot de passe existe deja");
            throw new ErreurException("vous avez déjà utilisé ce mot de passe !");
        }

        // update password
        user.setPassword(Utility.hashPassword(form.getNewPassword()));
        user.setChangePassword(ChangePassword.YES);
        user.setExpiredPassword(Utility.dateFromInteger(PASSWORD_DELAY, ChronoUnit.DAYS));
        user = userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    public Object forgetPassword(@Valid RegisterFirstStepForm form) {
        if (!Utility.checkEmail(form.getEmail())) {
            log.error("email invalide");
            throw new ErreurException("Email incorrecte !");
        }

        UserModel userModel = this.getUserByEmail(form.getEmail());

        if (userModel == null) {
            log.error("user not found");
            throw new ErreurException("Email n'existe pas !");
        }

        String token = UUID.randomUUID().toString();
        String link = FRONT_BASEURL + "/auth/recover-password?step=2&token=" + token;
        CodeOptModel code = new CodeOptModel(token, Utility.dateFromInteger(CODE_DELAY, ChronoUnit.MINUTES), userModel);
        boolean hasSend = genericService.sendMail("support@turbodeliveryapp.com", form.getEmail(),
                "Modification du mot de passe", genericService.template("Lien de modification du mot de passe",
                        "<p>Ce lien ne sera valide que pour les 5 prochaines minutes</p>\r\n" + //
                                "<a href=\"" + link
                                + "\" class=\"login-button\" style=\"color: white;\">cliquez ici</a>"));
        if (!hasSend) {
            log.error("email not sended");
            throw new ErreurException("mail non distribué !");
        }
        code = codeOptRepository.save(code);
        Map<String, Object> response = Map.of("link", link, "initBy", userModel);
        return ResponseEntity.ok(response);
    }

    public Object newPassword(@Valid NewPasswordForm form) {
        Optional<CodeOptModel> codeOpt = codeOptRepository.findFirstByCode(form.getToken());
        Date now = new Date();
        if (codeOpt.isEmpty()) {
            log.error("token invalid");
            throw new ErreurException("Token invalide !");
        }
        CodeOptModel code = codeOpt.get();
        UserModel user = code.getUser();
        if (code.getExpired().compareTo(now) < 0) {
            log.error("token expired");
            codeOptRepository.delete(code);
            throw new ErreurException("Code expiré !");
        }
        if (!Utility.passwordValidator(form.getNewPassword(), PASSWORD_LENGTH)) {
            return ResponseEntity.badRequest().body(
                    Report.getMessage("message",
                            "le mot de passe doit contenir au moins" + PASSWORD_LENGTH
                                    + " , dont une lettre majuscule,miniscule,chiffre et un caractere special",
                            "code", "CHP13"));
        }

        boolean isExist = userPasswordService.saveUserPassword(form.getNewPassword(), user);
        if (!isExist) {
            log.error("cet mot de passe existe deja");
            throw new ErreurException("vous avez déjà utilisé ce mot de passe !");
        }

        // update password
        user.setPassword(Utility.hashPassword(form.getNewPassword()));
        user.setChangePassword(ChangePassword.YES);
        user.setExpiredPassword(Utility.dateFromInteger(PASSWORD_DELAY, ChronoUnit.DAYS));
        user = userRepository.save(user);
        codeOptRepository.delete(code);
        return ResponseEntity.ok(user);
    }

    public Object profile() {
        return ResponseEntity.ok(genericService.getAuthUser());
    }

    public Object updateProfile(MultipartFile avatar, UpdateProfileForm form) {
        UserModel userM = genericService.getAuthUser();
        if (form.getRole() != null) {
            RoleModel role = roleService.getRoleById(form.getRole());
            userM.setRole(role);
        }
        if (form.getFirstName() != null && !form.getFirstName().isEmpty()) {
            userM.setFirstName(form.getFirstName());
        }
        if (form.getLastName() != null && !form.getLastName().isEmpty()) {
            userM.setLastName(form.getLastName());
        }
        if (form.getEmail() != null && !form.getEmail().isEmpty()) {
            if (!Utility.checkEmail(form.getEmail())) {
                log.error("email not allow");
                throw new ErreurException("Email invalide !");
            }
            userM.setEmail(form.getEmail());
        }
        if (form.getTelephone() != null && !form.getTelephone().isEmpty()) {
            userM.setTelephone(form.getTelephone());
        }

        if (avatar != null && !avatar.isEmpty()) {
            String extention = genericService.getFileExtension(avatar.getOriginalFilename());
            if (!extention.equalsIgnoreCase("png") && !extention.equalsIgnoreCase("jpg")) {
                log.error("file extension not correct. accept png or jpg");
                throw new ErreurException("L'image doit être au format png ou jpg !");
            }
            String fileName = genericService.generateFileName("avatar") + "." + extention;
            File file = new File(fileName);
            boolean fileIsSave = genericService.compressImage(avatar, file);
            if (!fileIsSave) {
                log.error("file not saved");
                throw new ErreurException("Une erreur est survenue lors du sauvegarde de l'image !");
            }

            userM.setAvatar(fileName);
            userM.setAvatarUrl(fileName);
        }

        userM = userRepository.save(userM);
        log.info("update user {}", userM.getId());
        return ResponseEntity.ok(userM);
    }

    public Object usersRestaurant(UUID restoId) {
        List<UserModel> users = userRepository.findAllByRestaurantIdAndDeletedFalse(restoId);
        return ResponseEntity.ok(users);
    }

}
