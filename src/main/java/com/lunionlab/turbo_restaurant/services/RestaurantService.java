package com.lunionlab.turbo_restaurant.services;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import com.lunionlab.turbo_restaurant.form.*;
import com.lunionlab.turbo_restaurant.objetvaleur.TypeCommission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.lunionlab.turbo_restaurant.Enums.DayOfWeekEnum;
import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.Enums.StatusEnum;
import com.lunionlab.turbo_restaurant.model.AccompagnementModel;
import com.lunionlab.turbo_restaurant.model.BoissonModel;
import com.lunionlab.turbo_restaurant.model.OpeningHoursModel;
import com.lunionlab.turbo_restaurant.model.OptionValeurModel;
import com.lunionlab.turbo_restaurant.model.OrderItemModel;
import com.lunionlab.turbo_restaurant.model.PlatModel;
import com.lunionlab.turbo_restaurant.model.RestaurantModel;
import com.lunionlab.turbo_restaurant.model.TypeCuisineRestaurantModel;
import com.lunionlab.turbo_restaurant.model.UserModel;
import com.lunionlab.turbo_restaurant.model.UserOrderM;
import com.lunionlab.turbo_restaurant.repository.AccompagnementRepo;
import com.lunionlab.turbo_restaurant.repository.BoissonRespository;
import com.lunionlab.turbo_restaurant.repository.OpeningHourRepo;
import com.lunionlab.turbo_restaurant.repository.OptionValeurRepo;
import com.lunionlab.turbo_restaurant.repository.PictureRestoRepository;
import com.lunionlab.turbo_restaurant.repository.PlatRepository;
import com.lunionlab.turbo_restaurant.repository.RestaurantRepository;
import com.lunionlab.turbo_restaurant.repository.TypeCuisineRestoRepository;
import com.lunionlab.turbo_restaurant.repository.UserOrderRepo;
import com.lunionlab.turbo_restaurant.repository.UserRepository;
import com.lunionlab.turbo_restaurant.response.OrderItemResponse;
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

    @Autowired
    OpeningHourRepo openingHourRepo;

    @Autowired
    UserOrderRepo userOrderRepo;

    @Autowired
    PlatRepository platRepository;

    @Autowired
    PagedResourcesAssembler<UserOrderM> assembler;

    @Autowired
    BoissonRespository boissonRespository;

    @Autowired
    AccompagnementRepo accompagnementRepo;

    @Autowired
    OptionValeurRepo optionValeurRepo;

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

        if (!Utility.checkEmail(form.getEmail())) {
            log.error("email invalid");
            return ResponseEntity.badRequest().body(Report.message("message", "le mail est invalide"));
        }

        Map<String, String> errors = new HashMap<>();

        // verification de la nullité des fichiers
        if (logoUrl == null || logoUrl.isEmpty()) {
            errors.put("logo", "vous devez soumettre une image");
            return ResponseEntity.badRequest().body(errors);
        }
        if (cniUrl == null || cniUrl.isEmpty()) {
            errors.put("cni", "vous devez soumettre un fichier pdf");
            return ResponseEntity.badRequest().body(errors);
        }
        if (docUrl == null || docUrl.isEmpty()) {
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
        if (user.getRestaurant() != null) {
            log.error("ce utilisateur a déjà un restaurant");
            return ResponseEntity.badRequest().body(Report.getMessage("message",
                    "vous n'êtes pas habilité à ajouter plusieurs restaurants", "code", "R0"));
        }
        // save restaurant
        RestaurantModel restaurant = new RestaurantModel(form.getNomEtablissement(), form.getDescription(),
                form.getEmail(), form.getTelephone(), form.getCodePostal(), form.getCommune(), form.getLocalisation(),
                form.getSiteWeb(), logo, logo, Utility.dateFromString(form.getDateService()), document, cni);
        restaurant.setLatitude(form.getLatitude());
        restaurant.setLongitude(form.getLongitude());
        restaurant.setIdLocation(form.getIdLocation());
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
        if (restaurant == null) {
            log.error("restaurant not found");
            return ResponseEntity.badRequest()
                    .body(Report.getMessage("message", "vous n'avez pas de restaurant", "code", "R0"));
        }
        if (logoUrl != null && !logoUrl.isEmpty()) {
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

        if (cniUrl != null && !cniUrl.isEmpty()) {
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

        if (docUrl != null && !docUrl.isEmpty()) {
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

        if (form.getLatitude() != null) {
            restaurant.setLatitude(form.getLatitude());
        }
        if (form.getLongitude() != null) {
            restaurant.setLongitude(form.getLongitude());
        }
        if (form.getIdLocation() != null && !form.getIdLocation().isEmpty()) {
            restaurant.setIdLocation(form.getIdLocation());

        }

        restaurant = restaurantRepository.save(restaurant);

        return ResponseEntity.ok(restaurant);
    }

    public Object getUserAuthRestaurant() {
        UserModel user = genericService.getAuthUser();
        List<TypeCuisineRestaurantModel> typecuisines = new ArrayList<>();
        if (user.getRestaurant() == null) {
            log.error("this user hasn't any restaurant");
            return ResponseEntity.badRequest()
                    .body(Report.message("message", "Cet utilisateur n'a pas encore ajouté son restaurant"));
        }
        typecuisines = typeCuisineRestoRepository.findByRestaurantAndDeleted(user.getRestaurant(), DeletionEnum.NO);
        Map<String, Object> response = new HashMap<>();
        response.put("restaurant", user.getRestaurant());
        response.put("typecuisine", typecuisines);
        return ResponseEntity.ok(response);
    }

    public Object getAllRestaurantNotValidated(Integer page) {
        List<Integer> status = new ArrayList<>();
        status.add(StatusEnum.RESTO_VALID_BY_AUTHSERVICE);
        status.add(StatusEnum.RESTO_VALID_BY_OPSMANAGER);
        Page<RestaurantModel> restaurants = restaurantRepository
                .findByStatusNotInAndDeletedOrderByDateCreationDesc(status, DeletionEnum.NO,
                        genericService.pagination(page));
        return ResponseEntity.ok(restaurants);
    }

    public Object getAllRestaurantValidByAuthService(Integer page) {
        Page<RestaurantModel> restaurants = restaurantRepository.findByStatusAndDeletedOrderByDateCreationDesc(
                StatusEnum.RESTO_VALID_BY_AUTHSERVICE, DeletionEnum.NO, genericService.pagination(page));
        return ResponseEntity.ok(restaurants);
    }

    public Object getAllRestaurantValidByOpsManager(Integer page) {
        Page<RestaurantModel> restaurants = restaurantRepository.findByStatusAndDeletedOrderByDateCreationDesc(
                StatusEnum.RESTO_VALID_BY_OPSMANAGER, DeletionEnum.NO, genericService.pagination(page));
        restaurants.forEach(restaurant -> {
            restaurant.setIsOpen(this.isOpen(restaurant));
            restaurantRepository.save(restaurant);
        });
        return ResponseEntity.ok(restaurants);
    }

    public Object restaurantValidatedByAuthService(UUID restoId) {
        List<Integer> statusAllow = List.of(StatusEnum.DEFAULT_DESABLE, StatusEnum.DEFAULT_ENABLE);
        Optional<RestaurantModel> restaurantOpt = restaurantRepository.findFirstByIdAndStatusInAndDeleted(restoId,
                statusAllow, DeletionEnum.NO);
        if (restaurantOpt.isEmpty()) {
            log.error("this restaurant not found");
            return Report.notFound("this restaurant not found");
        }
        RestaurantModel restaurant = restaurantOpt.get();
        restaurant.setStatus(StatusEnum.RESTO_VALID_BY_AUTHSERVICE);
        restaurant = restaurantRepository.save(restaurant);
        log.info("restaurant validated by auth service");
        return ResponseEntity.ok(restaurant);
    }

    public Object restaurantValidatedByOpsManager(UUID restoId) {
        Optional<RestaurantModel> restaurantOpt = restaurantRepository.findFirstByIdAndStatusAndDeleted(restoId,
                StatusEnum.RESTO_VALID_BY_AUTHSERVICE, DeletionEnum.NO);
        if (restaurantOpt.isEmpty()) {
            log.error("this restaurant not found");
            return Report.notFound("this restaurant not found");
        }
        RestaurantModel restaurant = restaurantOpt.get();
        restaurant.setStatus(StatusEnum.RESTO_VALID_BY_OPSMANAGER);
        restaurant = restaurantRepository.save(restaurant);
        log.info("restaurant validated by Ops manager");
        return ResponseEntity.ok(restaurant);
    }

    public Object restaurantDetail(UUID restoId) {
        Optional<RestaurantModel> restaurantOpt = restaurantRepository.findFirstByIdAndDeleted(restoId,
                DeletionEnum.NO);
        if (restaurantOpt.isEmpty()) {
            log.error("this restaurant isn't found");
            return ResponseEntity.badRequest().body("this restaurant isn't found");
        }
        return ResponseEntity.ok(restaurantOpt.get());
    }

    public Object searResto(@Valid SearchRestoForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais format des données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }
        Optional<RestaurantModel> restaurantOpt = restaurantRepository
                .findFirstByNomEtablissementContainingIgnoreCaseAndDeleted(form.getLibelle(), DeletionEnum.NO);
        if (restaurantOpt.isEmpty()) {
            log.error("datas not found");
            return ResponseEntity.badRequest().body("Aucune donnée trouvée");
        }
        return ResponseEntity.ok(restaurantOpt.get());
    }

    // init restaurant schedule

    public Object addOpeningHours(@Valid AddOpeningForm form, BindingResult result) {
        if (result.hasErrors()) {
            log.error("mauvais format des données");
            return ResponseEntity.badRequest().body(Report.getErrors(result));
        }

        if (!DayOfWeekEnum.DAYOFWEEK.contains(form.getDayOfWeek())) {
            log.error("day of week is invalide");
            return ResponseEntity.badRequest().body("dayOfWeek is invalide");
        }

        RestaurantModel restaurantM = genericService.getAuthUser().getRestaurant();
        if (restaurantM == null) {
            log.error("this user haven't a restaurant");
            return ResponseEntity.badRequest().body("Vous n'avez pas de restaurant");
        }

        Optional<OpeningHoursModel> openingHOpt = restaurantM.getOpeningHours().stream()
                .filter(item -> item.getDayOfWeek().equalsIgnoreCase(form.getDayOfWeek())).findFirst();

        if (openingHOpt.isPresent()) {
            OpeningHoursModel openingHoursM = openingHOpt.get();
            openingHoursM.setOpeningTime(Utility.StrToLocalTime(form.getOpeningTime()));
            openingHoursM.setClosingTime(Utility.StrToLocalTime(form.getClosingTime()));
        } else {
            OpeningHoursModel openingHoursM = new OpeningHoursModel();
            openingHoursM.setRestaurant(restaurantM);
            openingHoursM.setDayOfWeek(form.getDayOfWeek());
            openingHoursM.setOpeningTime(Utility.StrToLocalTime(form.getOpeningTime()));
            openingHoursM.setClosingTime(Utility.StrToLocalTime(form.getClosingTime()));
            restaurantM.getOpeningHours().add(openingHoursM);
        }
        restaurantM = restaurantRepository.save(restaurantM);
        log.error("opening hour save");
        return ResponseEntity.ok(restaurantM.getOpeningHours());

    }

    public Object getOpeningHours() {
        RestaurantModel restaurantM = genericService.getAuthUser().getRestaurant();
        if (restaurantM == null) {
            log.error("this user haven't a restaurant");
            return ResponseEntity.badRequest().body("Vous n'avez pas de restaurant");
        }
        return ResponseEntity.ok(openingHourRepo.findByRestaurantAndDeletedFalse(restaurantM));
    }

    public ResponseEntity<Boolean> restoIsOpen(UUID restoId) {
        LocalTime now = LocalTime.now();

        Optional<RestaurantModel> restaurantOpt = restaurantRepository.findFirstByIdAndStatusAndDeletedFalse(restoId,
                StatusEnum.RESTO_VALID_BY_OPSMANAGER);
        if (restaurantOpt.isEmpty()) {
            log.error("restaurant not found");
            return ResponseEntity.badRequest().body(false);
        }
        Optional<OpeningHoursModel> openingOpt = openingHourRepo.findFirstByDayOfWeekAndRestaurantAndDeletedFalse(
                Utility.getDayOfWeekFrench(),
                restaurantOpt.get());
        if (openingOpt.isEmpty()) {
            log.error("opening hours not found");
            return ResponseEntity.badRequest().body(false);
        }

        LocalTime openingTime = openingOpt.get().getOpeningTime();
        LocalTime closingTime = openingOpt.get().getClosingTime();
        Boolean isOpened;
        if (closingTime.isBefore(openingTime)) {
            // if closing time is before opening time, the restaurant is open if the current
            isOpened = now.compareTo(openingTime) >= 0 || now.compareTo(closingTime) <= 0;
        } else {
            // if closing time is after opening time, the restaurant is open if the current
            isOpened = now.compareTo(openingTime) >= 0 && now.compareTo(closingTime) <= 0;

        }

        return ResponseEntity.ok(isOpened);
    }

    public ResponseEntity<Boolean> saveUserOrder(UserOrderForm form) {
        // get restaurant
        Optional<RestaurantModel> restOpt = restaurantRepository.findFirstByIdAndStatusAndDeleted(
                UUID.fromString(form.getRestoId()),
                StatusEnum.RESTO_VALID_BY_OPSMANAGER, DeletionEnum.NO);

        if (restOpt.isEmpty()) {
            log.error("resto not found");
            return ResponseEntity.badRequest().body(false);
        }

        // check if userOrder exists
        Boolean isExist = userOrderRepo.existsByOrderIdAndOrderState(form.getId(), form.getOrderState());
        if (isExist) {
            log.error("this order is already exists");
            return ResponseEntity.badRequest().body(false);
        }

        UserOrderM userOrderM = new UserOrderM();
        userOrderM.setRestaurant(restOpt.get());
        userOrderM.setOrderId(form.getId());
        userOrderM.setOrderState(form.getOrderState());
        userOrderM.setDeliveryAddress(
                String.format("%s|%s|%s|%s|%s", form.getAdresseM().etage, form.getAdresseM().batName,
                        form.getAdresseM().infoSupl, form.getAdresseM().libelle, form.getAdresseM().numeroPorte));
        userOrderM.setPaymentMethod(form.getPaymentMethod());
        userOrderM.setRecipientName(form.getRecipientName());
        userOrderM.setRecipientPhone(form.getRecipientPhone());
        userOrderM.setTotalAmount(form.getTotalAmount());
        userOrderM.setDeliveryFee(form.getDeliveryFee());
        userOrderM.setServiceFee(form.getServiceFee());

        for (OrderItemResponse item : form.getOrderItemM()) {
            Optional<PlatModel> platM = platRepository.findFirstByIdAndRestaurantAndDeletedAndDisponibleTrue(
                    UUID.fromString(item.getPlatId()), restOpt.get(), DeletionEnum.NO);
            if (platM.isEmpty()) {
                continue;
            }
            BoissonModel boissonM = null;
            AccompagnementModel accompagnementM = null;
            OptionValeurModel optionValeurM = null;
            if (item.getDrinkId() != null && !item.getDrinkId().isEmpty()) {

                boissonM = boissonRespository
                        .findFirstByIdAndDeleted(UUID.fromString(item.getDrinkId()), DeletionEnum.NO).orElse(null);
            }
            if (item.getAccompId() != null && !item.getAccompId().isEmpty()) {
                accompagnementM = accompagnementRepo
                        .findFirstByIdAndDeleted(UUID.fromString(item.getAccompId()), DeletionEnum.NO).orElse(null);
            }
            if (item.getOptionValue() != null && !item.getOptionValue().isEmpty()) {

                optionValeurM = optionValeurRepo.findFirstByValeurAndDeletedFalse(item.getOptionValue())
                        .orElse(null);
            }
            OrderItemModel orderitem = new OrderItemModel(item.getPrice(), item.getQuantity(), platM.get(), userOrderM,
                    optionValeurM, accompagnementM, boissonM);
            userOrderM.getOrderItemM().add(orderitem);
        }
        userOrderM.setCodeOrder(Utility.generateOrderCode(userOrderM.getTotalAmount(),
                userOrderM.getRestaurant().getNomEtablissement()));
        userOrderRepo.save(userOrderM);

        return ResponseEntity.ok(true);
    }

    public ResponseEntity<?> getUserOrders() {
        RestaurantModel restaurant = genericService.getAuthUser().getRestaurant();
        if (restaurant == null) {
            log.error("this use hasn't a restaurant");
            return ResponseEntity.badRequest().body("Vous n'avez pas de commande pour le moment");
        }
        Page<UserOrderM> userOrderPage = userOrderRepo.findByRestaurantAndDeletedFalse(restaurant,
                genericService.pagination(0));
        if (userOrderPage.getContent().isEmpty()) {
            log.error("any user order found");
            return ResponseEntity.badRequest().body("Vous n'avez pas de commande pour le moment");
        }

        PagedModel<EntityModel<UserOrderM>> userOrderResource = assembler.toModel(userOrderPage);
        return ResponseEntity.ok(userOrderResource);
    }

    public Object rejectRestaurant(RejectRestoForm form) {
        Optional<RestaurantModel> restOpt = restaurantRepository.findFirstByIdAndDeleted(form.getRestoId(),
                DeletionEnum.NO);
        if (restOpt.isEmpty()) {
            log.error("restaurant not found");
            return ResponseEntity.badRequest().body("le restaurant specifié n'existe pas");
        }
        RestaurantModel restaurantM = restOpt.get();
        if (restaurantM.getStatus().intValue() != StatusEnum.DEFAULT_DESABLE.intValue()) {
            log.info("reject restaurant");
            restaurantM.setStatus(StatusEnum.DEFAULT_DESABLE);
        } else {
            log.info("ce restaurant a été rejecté");
            return ResponseEntity.badRequest().body(Report.message("message", "ce restaurant a déjà été rejecté"));
        }

        Boolean isSended = genericService.sendMail("info@turbodeliveryapp.com", restaurantM.getEmail(),
                "Status du compte", genericService.templateReject("Rejet des Infos", "\r\n"
                        + //
                        "                                <div class=\"code\">" + form.getMotif() + "</div>"));
        if (!isSended) {
            log.error("email not sended");
            return ResponseEntity.badRequest()
                    .body(Report.message("message", "mail non distrué"));
        }
        restaurantM = restaurantRepository.save(restaurantM);
        log.info("reject resto {}", restaurantM.getId());
        return ResponseEntity.ok(restaurantM);

    }

    public Boolean isOpen(RestaurantModel restaurant) {
        LocalTime now = LocalTime.now();
        Optional<OpeningHoursModel> openingOpt = openingHourRepo.findFirstByDayOfWeekAndRestaurantAndDeletedFalse(
                Utility.getDayOfWeekFrench(),
                restaurant);
        if (openingOpt.isEmpty()) {
            log.error("opening hours not found");
            return false;
        }
        LocalTime openingTime = openingOpt.get().getOpeningTime();
        LocalTime closingTime = openingOpt.get().getClosingTime();
        Boolean isOpened;
        if (closingTime.isBefore(openingTime)) {
            // if closing time is before opening time, the restaurant is open if the current
            isOpened = now.compareTo(openingTime) >= 0 || now.compareTo(closingTime) <= 0;
        } else {
            // if closing time is after opening time, the restaurant is open if the current
            isOpened = now.compareTo(openingTime) >= 0 && now.compareTo(closingTime) <= 0;

        }
        log.info("Restaurant with ID {} is currently {}", restaurant.getId(), isOpened ? "open" : "closed");
        return isOpened;
    }

    public void updateRestoCommission(UpdateRestoCommissionForm form) {
        TypeCommission type = form.getType();
        double commission = form.getCommission();
        RestaurantModel resto = restaurantRepository.findFirstByIdAndDeleted(form.getRestoId(), DeletionEnum.NO)
                .orElseThrow(() -> new RuntimeException("Le restaurant n'existe pas !"));
        resto.setTypeCommission(type);
        resto.setCommission(commission);
        this.restaurantRepository.save(resto);
    }
}
