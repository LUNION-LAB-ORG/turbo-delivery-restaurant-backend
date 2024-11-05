package com.lunionlab.turbo_restaurant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.lunionlab.turbo_restaurant.Enums.ChangePassword;
import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.Enums.JwtAudienceEnum;
import com.lunionlab.turbo_restaurant.Enums.StatusEnum;
import com.lunionlab.turbo_restaurant.form.ChangePasswordForm;
import com.lunionlab.turbo_restaurant.form.LoginForm;
import com.lunionlab.turbo_restaurant.form.NewPasswordForm;
import com.lunionlab.turbo_restaurant.form.RegisterFirstStepForm;
import com.lunionlab.turbo_restaurant.form.RegisterSecondStepForm;
import com.lunionlab.turbo_restaurant.form.RegisterThirdStepForm;
import com.lunionlab.turbo_restaurant.model.CodeOptModel;
import com.lunionlab.turbo_restaurant.model.UserModel;
import com.lunionlab.turbo_restaurant.repository.CodeOptRepository;
import com.lunionlab.turbo_restaurant.repository.UserRepository;
import com.lunionlab.turbo_restaurant.utilities.Report;
import com.lunionlab.turbo_restaurant.utilities.Utility;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
// import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
// import java.util.List;
import java.util.Optional;
import java.time.temporal.*;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    GenericService genericService;

    @Autowired
    CodeOptRepository codeOptRepository;

    @Autowired
    UserPasswordService userPasswordService;

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

    public Object getUserByUsername(String username) {
        return userRepository.findFirstByUsernameAndDeleted(username, DeletionEnum.NO).orElse(null);

    }

    public UserModel getUserByUsernameAndStatus(String username, Integer status) {
        return userRepository.findFirstByUsernameAndStatusAndDeleted(username, status, DeletionEnum.NO).orElse(null);
    }

    public UserModel getUserByEmail(String email) {
        return userRepository.findFirstByEmailAndDeleted(email, DeletionEnum.NO).orElse(null);
    }

    public Object login(@Valid LoginForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais format des données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }
        UserModel user = this.getUserByUsernameAndStatus(form.getUsername(), StatusEnum.DEFAULT_ENABLE);
        if (user == null) {
            log.error("user not found");
            return Report.unauthorize("login ou password incorrecte");
        }
        if (user.getChangePassword() == null || user.getChangePassword() == ChangePassword.No) {
            log.error("l'utilisateur doit changer son mot de passe");
            Map<String, Object> output = Map.of("message", "Veuillez modifier votre mot de passe", "code", "LOG10",
                    "user", user);
            return Report.unauthorize(output);
        }
        if (user.getStatus().intValue() == StatusEnum.DEFAULT_DESABLE.intValue()
                && user.getChangePassword() == ChangePassword.YES) {
            log.error("compte utilisateur desactivé");
            return Report.unauthorize(Report.getMessage("message",
                    "compte inactif. veuillez contacter l'administrateur", "code", "LOG11"));
        }

        Date now = new Date();

        if (Utility.checkPassword(form.getPassword(), user.getPassword())) {

            if (user.getExpiredPassword() == null) {
                user.setExpiredPassword(Utility.dateFromInteger(PASSWORD_DELAY, ChronoUnit.DAYS));
                user = userRepository.save(user);
            }

            if (user.getExpiredPassword().compareTo(now) < 0) {
                log.error("mot de passe expiré");
                return Report.unauthorize(Report.getMessage("message",
                        "mot de passe expiré. prière créer un nouveau mot de passe", "code", "LOG12"));
            }

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
                return Report.unauthorize(Report.getMessage("message", "vous avez atteint votre tentative de connexion",
                        "code", "LOG13"));
            }
        }

        return Report.unauthorize("username ou mot de passe incorrecte");
    }

    public Object registerFirstStep(@Valid RegisterFirstStepForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais format de données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }
        if (!Utility.checkEmail(form.getEmail())) {
            log.error("email invalide");
            return ResponseEntity.badRequest().body(Report.getMessage("message", "email invalide", "code", "EM10"));
        }
        Optional<UserModel> userOpt = userRepository.findFirstByEmail(form.getEmail());
        if (userOpt.isPresent()) {
            log.error("utilisateur déjè inscript");
            return ResponseEntity.badRequest()
                    .body(Report.getMessage("message", "Cet utilisateur existe deja", "code", "EM11"));
        }
        String codeOpt = genericService.generateOptCode();
        boolean hasSend = genericService.sendMail("support@turbodeliveryapp.com", form.getEmail(),
                "Code de confirmation",
                genericService.template("Votre code de confirmation pour TurboDelivery", "\r\n" + //
                        "                                <p>Ce code ne sera valide que pour les 5 prochaines minutes</p>\r\n"
                        + //
                        "                                <div class=\"code\">" + codeOpt + "</div>"));
        if (!hasSend) {
            log.error("email not sended");
            return ResponseEntity.badRequest()
                    .body(Report.getMessage("message", "mail non distrué", "code", "EM12"));
        }
        UserModel userModel = new UserModel(null, null, form.getEmail(), null, null);
        userModel = userRepository.save(userModel);
        CodeOptModel code = new CodeOptModel(codeOpt, Utility.dateFromInteger(CODE_DELAY, ChronoUnit.MINUTES),
                userModel);
        code = codeOptRepository.save(code);
        Map<String, Object> response = Map.of("codeOpt", code.getCode(), "user", userModel);
        return ResponseEntity.ok(response);

    }

    public Object registerSecondeStep(@Valid RegisterSecondStepForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais format des données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }
        Date now = new Date();
        Optional<CodeOptModel> codeOpt = codeOptRepository.findFirstByCode(form.getCode());
        if (codeOpt.isEmpty()) {
            log.error("code not foung");
            return ResponseEntity.badRequest().body(Report.getMessage("message", "code invalide", "code", "OPT10"));
        }
        CodeOptModel codeModel = codeOpt.get();
        if (codeModel.getExpired().compareTo(now) < 0) {
            log.error("code expiré");
            codeOptRepository.delete(codeModel);
            return ResponseEntity.badRequest().body(Report.getMessage("message", "code expiré", "code", "OPT11"));
        }

        codeOptRepository.delete(codeModel);

        return ResponseEntity.ok("code verification is succed");
    }

    public Object registerThirdStep(@Valid RegisterThirdStepForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais format des données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }

        Optional<UserModel> userOpt = userRepository.findFirstByEmail(form.getEmail());
        if (userOpt.isEmpty()) {
            log.error("user not found");
            return ResponseEntity.badRequest().body(Report.getMessage("message", "user not found", "code", "USER10"));
        }
        String password = Utility.generatePassword(PASSWORD_LENGTH);
        UserModel user = userOpt.get();
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setTelephone(form.getTelephone());
        user.setUsername(form.getUsername());
        user.setPassword(Utility.hashPassword(password));
        user.setExpiredPassword(Utility.dateFromInteger(PASSWORD_DELAY, ChronoUnit.DAYS));
        user.setChangePassword(ChangePassword.No);
        user = userRepository.save(user);
        Map<String, Object> response = Map.of("password", password, "user", user);
        log.info("user info save successfull");
        return ResponseEntity.ok(response);
    }

    public Object changeMyPassword(@Valid ChangePasswordForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais format de données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }

        UserModel user = this.getUserByUsernameAndStatus(form.getUsername(), StatusEnum.DEFAULT_ENABLE);
        if (user == null) {
            log.error("user not found");
            return ResponseEntity.badRequest()
                    .body(Report.getMessage("message", "username incorrecte", "code", "CHP10"));
        }

        if (!Utility.checkPassword(form.getOldPassword(), user.getPassword())) {
            log.error("old password isn't trusted");
            return ResponseEntity.badRequest()
                    .body(Report.getMessage("message", "ancien de mot de pass incorrecte", "code", "CHP11"));
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
            return ResponseEntity.badRequest().body(
                    Report.getMessage("message", "vous avez déjà utiliser ce mot de pass", "code", "CHP12"));
        }

        // update password
        user.setPassword(Utility.hashPassword(form.getNewPassword()));
        user.setChangePassword(ChangePassword.YES);
        user.setExpiredPassword(Utility.dateFromInteger(PASSWORD_DELAY, ChronoUnit.DAYS));
        user = userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    public Object forgetPassword(@Valid RegisterFirstStepForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais format des données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }
        if (!Utility.checkEmail(form.getEmail())) {
            log.error("email invalide");
            return ResponseEntity.badRequest().body(Report.getMessage("message", "email invalide", "code", "EM10"));
        }

        UserModel userModel = this.getUserByEmail(form.getEmail());

        if (userModel == null) {
            log.error("user not found");
            return ResponseEntity.badRequest().body(Report.getMessage("message", "email not exists", "code", "EM13"));
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
            return ResponseEntity.badRequest()
                    .body(Report.getMessage("message", "mail non distrué", "code", "EM12"));
        }
        code = codeOptRepository.save(code);
        Map<String, Object> response = Map.of("link", link, "initBy", userModel);
        return ResponseEntity.ok(response);
    }

    public Object newPassword(@Valid NewPasswordForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais format des données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }
        Optional<CodeOptModel> codeOpt = codeOptRepository.findFirstByCode(form.getToken());
        Date now = new Date();
        if (codeOpt.isEmpty()) {
            log.error("token invalid");
            return ResponseEntity.badRequest().body(Report.getMessage("message", "token invalide", "code", "OPT10"));
        }
        CodeOptModel code = codeOpt.get();
        UserModel user = code.getUser();
        if (code.getExpired().compareTo(now) < 0) {
            log.error("token expired");
            codeOptRepository.delete(code);
            return ResponseEntity.badRequest().body(Report.getMessage("message", "code expiré", "code", "OPT11"));
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
            return ResponseEntity.badRequest().body(
                    Report.getMessage("message", "vous avez déjà utiliser ce mot de pass", "code", "CHP12"));
        }

        // update password
        user.setPassword(Utility.hashPassword(form.getNewPassword()));
        user.setChangePassword(ChangePassword.YES);
        user.setExpiredPassword(Utility.dateFromInteger(PASSWORD_DELAY, ChronoUnit.DAYS));
        user = userRepository.save(user);
        codeOptRepository.delete(code);
        return ResponseEntity.ok(user);
    }
}
