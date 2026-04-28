package com.lunionlab.turbo_restaurant.services;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lunionlab.turbo_restaurant.entities.AccompagnementModel;
import com.lunionlab.turbo_restaurant.entities.BoissonModel;
import com.lunionlab.turbo_restaurant.entities.ContactRestaurantModel;
import com.lunionlab.turbo_restaurant.entities.OpeningHoursModel;
import com.lunionlab.turbo_restaurant.entities.OptionValeurModel;
import com.lunionlab.turbo_restaurant.entities.OrderItemModel;
import com.lunionlab.turbo_restaurant.entities.PictureRestaurantModel;
import com.lunionlab.turbo_restaurant.entities.PlatModel;
import com.lunionlab.turbo_restaurant.entities.RestaurantModel;
import com.lunionlab.turbo_restaurant.entities.TypeCuisineRestaurantModel;
import com.lunionlab.turbo_restaurant.entities.UserModel;
import com.lunionlab.turbo_restaurant.entities.UserOrderM;
import com.lunionlab.turbo_restaurant.enums.DayOfWeekEnum;
import com.lunionlab.turbo_restaurant.enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.enums.StatusEnum;
import com.lunionlab.turbo_restaurant.enums.TypeCommission;
import com.lunionlab.turbo_restaurant.exceptions.ErreurException;
import com.lunionlab.turbo_restaurant.exceptions.ObjetNonTrouveException;
import com.lunionlab.turbo_restaurant.forms.AddOpeningForm;
import com.lunionlab.turbo_restaurant.forms.CreateRestaurantForm;
import com.lunionlab.turbo_restaurant.forms.CreateRestaurantV2Form;
import com.lunionlab.turbo_restaurant.forms.RejectRestoForm;
import com.lunionlab.turbo_restaurant.forms.SearchRestoForm;
import com.lunionlab.turbo_restaurant.forms.UpdateRestaurant;
import com.lunionlab.turbo_restaurant.forms.UpdateRestoCommissionForm;
import com.lunionlab.turbo_restaurant.forms.UpdateRestaurantV2Form;
import com.lunionlab.turbo_restaurant.forms.UserOrderForm;
import com.lunionlab.turbo_restaurant.repositories.AccompagnementRepo;
import com.lunionlab.turbo_restaurant.repositories.BoissonRespository;
import com.lunionlab.turbo_restaurant.repositories.OpeningHourRepo;
import com.lunionlab.turbo_restaurant.repositories.OptionValeurRepo;
import com.lunionlab.turbo_restaurant.repositories.PictureRestoRepository;
import com.lunionlab.turbo_restaurant.repositories.PlatRepository;
import com.lunionlab.turbo_restaurant.repositories.RestaurantRepository;
import com.lunionlab.turbo_restaurant.repositories.TypeCuisineRestoRepository;
import com.lunionlab.turbo_restaurant.repositories.UserOrderRepo;
import com.lunionlab.turbo_restaurant.repositories.UserRepository;
import com.lunionlab.turbo_restaurant.responses.OrderItemResponse;
import com.lunionlab.turbo_restaurant.utilities.Utility;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final BoissonRespository boissonRespository;
    private final AccompagnementRepo accompagnementRepo;
    private final OptionValeurRepo optionValeurRepo;
    private final UserRepository userRepository;
    private final TypeCuisineRestoRepository typeCuisineRestoRepository;
    private final OpeningHourRepo openingHourRepo;
    private final UserOrderRepo userOrderRepo;
    private final PlatRepository platRepository;

    private final GenericService genericService;
    private final RoleService roleService;

    @Autowired
    PagedResourcesAssembler<UserOrderM> assembler;

    @Value("${backend.server.address}") 
    private String BACKEND;

    public RestaurantService(
        RestaurantRepository restaurantRepository,
        BoissonRespository boissonRespository,
        AccompagnementRepo accompagnementRepo,
        OptionValeurRepo optionValeurRepo,
        UserRepository userRepository,
        PictureRestoRepository pictureRestoRepository,
        TypeCuisineRestoRepository typeCuisineRestoRepository,
        OpeningHourRepo openingHourRepo,
        UserOrderRepo userOrderRepo,
        PlatRepository platRepository,
        GenericService genericService,
        RoleService roleService
    ) {
        this.restaurantRepository = restaurantRepository;
        this.boissonRespository = boissonRespository;
        this.accompagnementRepo = accompagnementRepo;
        this.optionValeurRepo = optionValeurRepo;
        this.userRepository = userRepository;
        this.typeCuisineRestoRepository = typeCuisineRestoRepository;
        this.openingHourRepo = openingHourRepo;
        this.userOrderRepo = userOrderRepo;
        this.platRepository = platRepository;
        this.genericService = genericService;
        this.roleService = roleService;
    }

    @Transactional
    public Object createRestaurant(
            MultipartFile logoUrl, MultipartFile cniUrl, MultipartFile docUrl,
            @Valid CreateRestaurantForm form
    ) {
        String email = form.getEmail();
        if (restaurantRepository.existsByEmail(email)) {
            throw new ErreurException("L'email existe déjà !");
        }

        boolean isExist = restaurantRepository
                .existsByNomEtablissementAndEmailAndDeleted(form.getNomEtablissement(), email, DeletionEnum.NO);

        if (isExist) {
            log.error("le restaurant existe déjà");
            throw new ErreurException("Le restaurant existe déjà !");
        }

        if (!Utility.checkEmail(email)) {
            log.error("email invalid");
            throw new ErreurException("L'email est invalide !");
        }

        // verification de la nullité des fichiers
        if (logoUrl == null || logoUrl.isEmpty()) {
            throw new ErreurException("Vous devez soumettre un logo !");
        }

        if (cniUrl == null || cniUrl.isEmpty()) {
            throw new ErreurException("Vous devez soumettre un fichier pdf pour le CNI !");
        }

        if (docUrl == null || docUrl.isEmpty()) {
            throw new ErreurException("Vous devez soumettre un fichier pdf pour le document !");
        }

        String logo = null;
        String cni = null;
        String document = null;

        if (!logoUrl.isEmpty()) {
            String logExtension = genericService.getFileExtension(logoUrl.getOriginalFilename());
            if (!logExtension.equalsIgnoreCase("png") &&
                    !logExtension.equalsIgnoreCase("jpg") &&
                    !logExtension.equalsIgnoreCase("jpeg")
            ) {
                log.error(logExtension);
                throw new ErreurException("Le logo doit être au format jpg, jpeg ou png !");
            }
            logo = genericService.generateFileName("logo") + "." + logExtension;
            File logFile = new File(logo);
            try {
                logoUrl.transferTo(logFile.toPath());
            } catch (Exception e) {
                throw new ErreurException("Erreur : " + e.getMessage());
            }

        }

        if (!cniUrl.isEmpty()) {
            String cniExtension = genericService.getFileExtension(cniUrl.getOriginalFilename());
            if (!cniExtension.equalsIgnoreCase("pdf")) {
                throw new ErreurException("La cni doit être au format pdf !");
            }
            cni = genericService.generateFileName("cni") + "." + cniExtension;
            File cniFile = new File(cni);
            try {
                cniUrl.transferTo(cniFile.toPath());
            } catch (Exception e) {
                throw new ErreurException("Erreur : " + e.getMessage());
            }
        }

        if (!docUrl.isEmpty()) {
            String docExtension = genericService.getFileExtension(docUrl.getOriginalFilename());
            if (!docExtension.equalsIgnoreCase("pdf")) {
                throw new ErreurException("Le registre de commerce doit être au format pdf !");
            }

            document = genericService.generateFileName("document") + "." + docExtension;
            File docFile = new File(document);
            try {
                docUrl.transferTo(docFile.toPath());
            } catch (Exception e) {
                throw new ErreurException("Erreur : " + e.getMessage());
            }
        }

        UserModel user = genericService.getAuthUser();
        if (user == null) {
            log.error("user not authenticated");
            throw new ErreurException("Nous ne pouvons pas donner suite à votre operation !");
        }

        if (user.getRestaurant() != null) {
            log.error("ce utilisateur a déjà un restaurant");
            throw new ErreurException("Vous n'êtes pas habilité à ajouter plusieurs restaurants !");
        }

        String apiKeyResto = Utility.genererNouveauApiKeyRestaurant();
        // save restaurant
        RestaurantModel restaurant = new RestaurantModel(
                form.getNomEtablissement(), form.getDescription(),
                email, form.getTelephone(), form.getCodePostal(),
                form.getCommune(), form.getLocalisation(), form.getSiteWeb(),
                logo, logo, Utility.dateFromString(form.getDateService()), document, cni
        );
        restaurant.setLatitude(form.getLatitude());
        restaurant.setLongitude(form.getLongitude());
        restaurant.setIdLocation(form.getIdLocation());
        restaurant.setApiKeyResto(apiKeyResto);
        restaurant.setStatus(StatusEnum.RESTO_VALID_BY_OPSMANAGER);
        restaurant = restaurantRepository.save(restaurant);

        user.setRole(roleService.getAdmin());
        user.setRestaurant(restaurant);
        user = userRepository.save(user);
        Map<String, Object> response = Map.of("restaurant", restaurant, "createdBy", user);

        this.notifierErp(restaurant.getNomEtablissement());

        return ResponseEntity.ok(response);
    }

    @Transactional
    public Object updateRestaurant(
            MultipartFile logoUrl, MultipartFile cniUrl,
            MultipartFile docUrl, UpdateRestaurant form
    ) {
        UserModel userAuth = genericService.getAuthUser();
        RestaurantModel restaurant = userAuth.getRestaurant();
        if (restaurant == null) {
            log.error("restaurant not found");
            throw new ErreurException("Vous n'avez pas de restaurant !");
        }
        if (logoUrl != null && !logoUrl.isEmpty()) {
            String logExtension = genericService.getFileExtension(logoUrl.getOriginalFilename());
            if (!logExtension.equalsIgnoreCase("png") &&
                    !logExtension.equalsIgnoreCase("jpg") &&
                    !logExtension.equalsIgnoreCase("jpeg")
            ) {
                log.error(logExtension);
                throw new ErreurException("Le logo doit etre au format jpg, jpeg ou png !");
            }

            String logo = genericService.generateFileName("logo");
            File logFile = new File(logo + "." + logExtension);
            try {
                logoUrl.transferTo(logFile.toPath());
                restaurant.setLogo(logo);
                restaurant.setLogo_Url(logo);
            } catch (IllegalStateException | IOException e) {
                throw new ErreurException(e.getMessage());
            }
        }

        if (cniUrl != null && !cniUrl.isEmpty()) {
            String cniExtension = genericService.getFileExtension(cniUrl.getOriginalFilename());
            if (!cniExtension.equalsIgnoreCase("pdf")) {
                throw new ErreurException("La cni doit être au format pdf !");
            }

            String cni = genericService.generateFileName("cni") + "." + cniExtension;
            File cniFile = new File(cni);
            try {
                cniUrl.transferTo(cniFile.toPath());
                restaurant.setCni(cni);
            } catch (IllegalStateException | IOException e) {
                throw new ErreurException(e.getMessage());
            }
        }

        if (docUrl != null && !docUrl.isEmpty()) {
            String docExtension = genericService.getFileExtension(docUrl.getOriginalFilename());
            if (!docExtension.equalsIgnoreCase("pdf")) {
                throw new ErreurException("Le registre de commerce doit etre au format pdf !");
            }

            String document = genericService.generateFileName("document") + "." + docExtension;
            File docFile = new File(document);
            try {
                docUrl.transferTo(docFile.toPath());
                restaurant.setDocumentUrl(document);
            } catch (IllegalStateException | IOException e) {
                throw new ErreurException(e.getMessage());
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
        List<TypeCuisineRestaurantModel> typecuisines;
        if (user.getRestaurant() == null) {
            log.error("this user hasn't any restaurant");
            throw new ErreurException("Cet utilisateur n'a pas encore ajouté son restaurant !");
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

    public Object getAllRestaurantValidByOpsManager() {
        List<RestaurantModel> restaurants = restaurantRepository.findByStatusAndDeletedOrderByDateCreationDesc(
                StatusEnum.RESTO_VALID_BY_OPSMANAGER, DeletionEnum.NO);
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
            throw new ObjetNonTrouveException("Cet restaurant n'existe pas !");
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
            throw new ObjetNonTrouveException("Cet restaurant n'existe pas !");
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
            throw new ObjetNonTrouveException("Cet restaurant n'existe pas !");
        }
        return ResponseEntity.ok(restaurantOpt.get());
    }

    public Object optionalDetail(UUID restoId) {
        Optional<RestaurantModel> restaurantOpt = restaurantRepository
                .findFirstByIdAndDeleted(restoId, DeletionEnum.NO);
        return ResponseEntity.ok(restaurantOpt);
    }

    public Object searResto(@Valid SearchRestoForm form) {
        Optional<RestaurantModel> restaurantOpt = restaurantRepository
                .findFirstByNomEtablissementContainingIgnoreCaseAndDeleted(form.getLibelle(), DeletionEnum.NO);
        if (restaurantOpt.isEmpty()) {
            log.error("datas not found");
            throw new ObjetNonTrouveException("Aucune donnée trouvée !");
        }
        return ResponseEntity.ok(restaurantOpt.get());
    }

    // init restaurant schedule

    public Object addOpeningHours(@Valid AddOpeningForm form) {

        if (!DayOfWeekEnum.DAYOFWEEK.contains(form.getDayOfWeek())) {
            log.error("day of week is invalide");
            throw new ObjetNonTrouveException("Le jour de la semaine " + form.getDayOfWeek() + " est invalide !");
        }

        RestaurantModel restaurantM = genericService.getAuthUser().getRestaurant();
        if (restaurantM == null) {
            log.error("this user haven't a restaurant");
            throw new ErreurException("Vous n'avez pas de restaurant !");
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
            throw new ErreurException("Vous n'avez pas de restaurant !");
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
            throw new ErreurException("Vous n'avez pas de commande pour le moment !");
        }

        Page<UserOrderM> userOrderPage = userOrderRepo.findByRestaurantAndDeletedFalse(restaurant,
                genericService.pagination(0));
        if (userOrderPage.getContent().isEmpty()) {
            throw new ErreurException("Vous n'avez pas de commande pour le moment !");
        }

        PagedModel<EntityModel<UserOrderM>> userOrderResource = assembler.toModel(userOrderPage);
        return ResponseEntity.ok(userOrderResource);
    }

    public Object rejectRestaurant(RejectRestoForm form) {
        Optional<RestaurantModel> restOpt = restaurantRepository.findFirstByIdAndDeleted(form.getRestoId(),
                DeletionEnum.NO);
        if (restOpt.isEmpty()) {
            throw new ErreurException("Le restaurant specifié n'existe pas !");
        }
        RestaurantModel restaurantM = restOpt.get();
        if (restaurantM.getStatus().intValue() != StatusEnum.DEFAULT_DESABLE.intValue()) {
            restaurantM.setStatus(StatusEnum.DEFAULT_DESABLE);
        } else {
            throw new ErreurException("Ce restaurant a déjà été rejecté !");
        }

        Boolean isSended = genericService.sendMail("info@turbodeliveryapp.com", restaurantM.getEmail(),
                "Status du compte", genericService.templateReject("Rejet des Infos", "\r\n"
                        + //
                        "                                <div class=\"code\">" + form.getMotif() + "</div>"));
        if (!isSended) {
            throw new ErreurException("Mail non distribué !");
        }
        restaurantM = restaurantRepository.save(restaurantM);
        return ResponseEntity.ok(restaurantM);
    }

    public Boolean isOpen(RestaurantModel restaurant) {
        LocalTime now = LocalTime.now();
        Optional<OpeningHoursModel> openingOpt = openingHourRepo.findFirstByDayOfWeekAndRestaurantAndDeletedFalse(
                Utility.getDayOfWeekFrench(),
                restaurant);
                
        if (openingOpt.isEmpty()) {
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
        return isOpened;
    }

    public void updateRestoCommission(UpdateRestoCommissionForm form) {
        TypeCommission type = form.getType();
        double commission = form.getCommission();
        String methodRecouvrement = form.getMethodRecouvrement();
        RestaurantModel resto = restaurantRepository.findFirstByIdAndDeleted(form.getRestoId(), DeletionEnum.NO)
                .orElseThrow(() -> new RuntimeException("Le restaurant n'existe pas !"));
        resto.setTypeCommission(type);
        resto.setCommission(commission);
        resto.setMethodRecouvrement(methodRecouvrement);
        this.restaurantRepository.save(resto);
    }


    private List<OpeningHoursModel> parseOpeningHours(Map<String, String> params, RestaurantModel restaurant) {
        Pattern pattern = Pattern.compile("^openingHours\\[(\\d+)\\]\\[(\\w+)\\]$");
        Map<Integer, OpeningHoursModel> map = new TreeMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            Matcher m = pattern.matcher(entry.getKey());
            if (m.matches()) {
                int idx = Integer.parseInt(m.group(1));
                String field = m.group(2);
                OpeningHoursModel oh = map.computeIfAbsent(idx, k -> {
                    OpeningHoursModel o = new OpeningHoursModel();
                    o.setRestaurant(restaurant);
                    return o;
                });
                switch (field) {
                    case "dayOfWeek" -> oh.setDayOfWeek(entry.getValue().toUpperCase());
                    case "openingTime" -> {
                        if (entry.getValue() != null && !entry.getValue().isEmpty())
                            oh.setOpeningTime(Utility.StrToLocalTime(entry.getValue()));
                    }
                    case "closingTime" -> {
                        if (entry.getValue() != null && !entry.getValue().isEmpty())
                            oh.setClosingTime(Utility.StrToLocalTime(entry.getValue()));
                    }
                    case "closed" -> oh.setClosed(Boolean.parseBoolean(entry.getValue()));
                }
            }
        }
        return new ArrayList<>(map.values());
    }

    private void notifierErp(String nomEtablissement) {
        String message = "Un nouveau restaurant du nom de " + nomEtablissement + " vient d'être créé !";
        // String endpoint = BACKEND + "/erp/notification/notifier/erp";
        String endpoint = "https://backend-prod.turbodeliveryapp.com/api/erp/notification/notifier/erp";
        genericService.httpPost(endpoint, message);
    }

    public ResponseEntity<List<RestaurantModel>> getRestaurantsByTypeCommission(TypeCommission typeCommission) {
        List<RestaurantModel> restaurants = restaurantRepository.findAllByTypeCommissionAndDeletedFalse(typeCommission);
        return ResponseEntity.ok(restaurants);
    }

    public ResponseEntity<Page<RestaurantModel>> getRestaurantsByTypeCommission(TypeCommission typeCommission, int page, int size) {
        Pageable pageable = genericService.pagination(page, size);
        Page<RestaurantModel> restaurants = restaurantRepository.findAllByTypeCommissionAndDeletedFalse(typeCommission, pageable);
        return ResponseEntity.ok(restaurants);
    }

    public Page<RestaurantModel> listRestaurants(String nomEtablissement, String localisation, String email, String telephone, String commune, String methodRecouvrement, String typeCommission, Pageable pageable) {
        return restaurantRepository.findWithFilters(nomEtablissement, localisation, email, telephone, commune, methodRecouvrement, typeCommission, pageable);
    }

    public Map<String, Object> getPartenairesStats(String search, String localisation, String email,
            String telephone, String commune, String methodRecouvrement) {
        String s = (search == null || search.isBlank()) ? null : search;
        String loc = (localisation == null || localisation.isBlank()) ? null : localisation;
        String em = (email == null || email.isBlank()) ? null : email;
        String tel = (telephone == null || telephone.isBlank()) ? null : telephone;
        String com = (commune == null || commune.isBlank()) ? null : commune;
        String mr = (methodRecouvrement == null || methodRecouvrement.isBlank()) ? null : methodRecouvrement;

        Map<String, Object> row = restaurantRepository.getPartenairesStats(s, loc, em, tel, com, mr);

        long total = row.get("total") == null ? 0L : ((Number) row.get("total")).longValue();
        long pourcentage = row.get("pourcentage") == null ? 0L : ((Number) row.get("pourcentage")).longValue();
        long fixe = row.get("fixe") == null ? 0L : ((Number) row.get("fixe")).longValue();
        long quotidien = row.get("quotidien") == null ? 0L : ((Number) row.get("quotidien")).longValue();
        long hebdomadaire = row.get("hebdomadaire") == null ? 0L : ((Number) row.get("hebdomadaire")).longValue();
        long biHebdomadaire = row.get("quinzaine") == null ? 0L : ((Number) row.get("quinzaine")).longValue();
        long mensuel = row.get("mensuel") == null ? 0L : ((Number) row.get("mensuel")).longValue();

        Map<String, Object> commissions = new java.util.LinkedHashMap<>();
        commissions.put("pourcentage", pourcentage);
        commissions.put("fixe", fixe);

        Map<String, Object> cyclesPaiement = new java.util.LinkedHashMap<>();
        cyclesPaiement.put("quotidien", quotidien);
        cyclesPaiement.put("hebdomadaire", hebdomadaire);
        cyclesPaiement.put("biHebdomadaire", biHebdomadaire);
        cyclesPaiement.put("mensuel", mensuel);

        Map<String, Object> result = new java.util.LinkedHashMap<>();
        result.put("totalPartenaires", total);
        result.put("commissions", commissions);
        result.put("cyclesPaiement", cyclesPaiement);
        return result;
    }

    public Optional<RestaurantModel> getRestaurantById(UUID id) {
        return restaurantRepository.findById(id);
    }

    public Object activateRestaurant(UUID id) {
        RestaurantModel restaurant = restaurantRepository.findFirstByIdAndDeleted(id, DeletionEnum.NO)
                .orElseThrow(() -> new ErreurException("Le restaurant specifié n'existe pas !"));
        if (restaurant.getStatus() != null
                && restaurant.getStatus().intValue() == StatusEnum.RESTO_VALID_BY_OPSMANAGER.intValue()) {
            throw new ErreurException("Ce partenaire est déjà actif !");
        }
        restaurant.setStatus(StatusEnum.RESTO_VALID_BY_OPSMANAGER);
        return ResponseEntity.ok(restaurantRepository.save(restaurant));
    }

    public Object deactivateRestaurant(UUID id) {
        RestaurantModel restaurant = restaurantRepository.findFirstByIdAndDeleted(id, DeletionEnum.NO)
                .orElseThrow(() -> new ErreurException("Le restaurant specifié n'existe pas !"));
        if (restaurant.getStatus() != null
                && restaurant.getStatus().intValue() == StatusEnum.DEFAULT_DESABLE.intValue()) {
            throw new ErreurException("Ce partenaire est déjà désactivé !");
        }
        restaurant.setStatus(StatusEnum.DEFAULT_DESABLE);
        return ResponseEntity.ok(restaurantRepository.save(restaurant));
    }

    public Object softDeleteRestaurant(UUID id) {
        RestaurantModel restaurant = restaurantRepository.findFirstByIdAndDeleted(id, DeletionEnum.NO)
                .orElseThrow(() -> new ErreurException("Le restaurant specifié n'existe pas !"));
        restaurant.setDeleted(DeletionEnum.YES);
        restaurantRepository.save(restaurant);
        return ResponseEntity.ok(Map.of(
                "message", "Partenaire supprimé avec succès",
                "id", id
        ));
    }

    public Object forceDeleteRestaurant(UUID id) {
        RestaurantModel restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ErreurException("Le restaurant specifié n'existe pas !"));
        restaurantRepository.delete(restaurant);
        return ResponseEntity.ok(Map.of(
                "message", "Partenaire supprimé définitivement",
                "id", id
        ));
    }

    @Transactional
    public Object createRestaurantV2(
            @Valid CreateRestaurantV2Form form,
            MultipartFile logo,
            MultipartFile coverImage,
            MultipartFile[] pictures,
            MultipartFile document,
            Map<String, String> allParams) {

        UserModel user = genericService.getAuthUser();
        if (user != null && user.getRestaurant() != null) {
            throw new ErreurException("Vous n'êtes pas habilité à ajouter plusieurs restaurants !");
        }

        // Validation email si fourni
        String email = form.getEmail();
        if (email != null && !email.isEmpty()) {
            if (!Utility.checkEmail(email)) {
                throw new ErreurException("L'email est invalide !");
            }
            if (restaurantRepository.existsByEmail(email)) {
                throw new ErreurException("Cet email est déjà utilisé !");
            }
        }

        // Logo requis
        if (logo == null || logo.isEmpty()) {
            throw new ErreurException("Le logo est requis !");
        }
        String logoExtension = genericService.getFileExtension(logo.getOriginalFilename());
        if (!logoExtension.equalsIgnoreCase("png") &&
                !logoExtension.equalsIgnoreCase("jpg") &&
                !logoExtension.equalsIgnoreCase("jpeg")) {
            throw new ErreurException("Le logo doit être au format jpg, jpeg ou png !");
        }
        String logoPath = genericService.generateFileName("logo") + "." + logoExtension;
        try {
            logo.transferTo(new File(logoPath).toPath());
        } catch (IOException e) {
            throw new ErreurException("Erreur lors de la sauvegarde du logo : " + e.getMessage());
        }

        // CoverImage optionnel
        String coverImagePath = null;
        if (coverImage != null && !coverImage.isEmpty()) {
            String coverExtension = genericService.getFileExtension(coverImage.getOriginalFilename());
            if (!coverExtension.equalsIgnoreCase("png") &&
                    !coverExtension.equalsIgnoreCase("jpg") &&
                    !coverExtension.equalsIgnoreCase("jpeg")) {
                throw new ErreurException("L'image de couverture doit être au format jpg, jpeg ou png !");
            }
            coverImagePath = genericService.generateFileName("cover") + "." + coverExtension;
            try {
                coverImage.transferTo(new File(coverImagePath).toPath());
            } catch (IOException e) {
                throw new ErreurException("Erreur lors de la sauvegarde de l'image de couverture : " + e.getMessage());
            }
        }

        // Document optionnel
        String documentPath = null;
        if (document != null && !document.isEmpty()) {
            String docExtension = genericService.getFileExtension(document.getOriginalFilename());
            documentPath = genericService.generateFileName("document") + "." + docExtension;
            try {
                document.transferTo(new File(documentPath).toPath());
            } catch (IOException e) {
                throw new ErreurException("Erreur lors de la sauvegarde du document : " + e.getMessage());
            }
        }

        // Création du restaurant
        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setNomEtablissement(form.getNomEtablissement());
        restaurant.setTelephone(form.getTelephone());
        restaurant.setEmail(email);
        restaurant.setLocalisation(form.getLocalisation());
        restaurant.setCommune(form.getCommune());
        restaurant.setCodePostal(form.getCodePostal());
        restaurant.setSiteWeb(form.getSiteWeb());
        restaurant.setDescription(form.getDescription());
        restaurant.setLogo(logoPath);
        restaurant.setLogo_Url(logoPath);
        restaurant.setCoverImage(coverImagePath);
        restaurant.setCoverImageUrl(coverImagePath);
        restaurant.setDocumentUrl(documentPath);
        restaurant.setDocumentType(form.getDocumentType());
        restaurant.setApiKeyResto(Utility.genererNouveauApiKeyRestaurant());
        restaurant.setStatus(StatusEnum.RESTO_VALID_BY_OPSMANAGER);

        if (form.getTypeCommission() != null && !form.getTypeCommission().isEmpty()) {
            try {
                restaurant.setTypeCommission(TypeCommission.valueOf(form.getTypeCommission().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ErreurException("Type de commission invalide : FIXE ou POURCENTAGE attendu !");
            }
        }
        if (form.getCommission() != null) {
            restaurant.setCommission(form.getCommission());
        }
        if (form.getMethodRecouvrement() != null && !form.getMethodRecouvrement().isEmpty()) {
            restaurant.setMethodRecouvrement(form.getMethodRecouvrement());
        }

        restaurant = restaurantRepository.save(restaurant);

        // Photos de l'établissement
        if (pictures != null) {
            for (MultipartFile pic : pictures) {
                if (pic == null || pic.isEmpty()) continue;
                String picExtension = genericService.getFileExtension(pic.getOriginalFilename());
                if (!picExtension.equalsIgnoreCase("png") &&
                        !picExtension.equalsIgnoreCase("jpg") &&
                        !picExtension.equalsIgnoreCase("jpeg")) continue;
                String picPath = genericService.generateFileName("picture_restaurant") + "." + picExtension;
                genericService.compressImage(pic, new File(picPath));
                restaurant.getPictures().add(new PictureRestaurantModel(picPath, restaurant));
            }
        }

        // Horaires d'ouverture
        for (OpeningHoursModel oh : parseOpeningHours(allParams, restaurant)) {
            restaurant.getOpeningHours().add(oh);
        }

        // Contacts
        int i = 0;
        while (allParams.containsKey("contact_nom_" + i)) {
            String nom = allParams.get("contact_nom_" + i);
            String tel = allParams.getOrDefault("contact_telephone_" + i, "");
            if (nom != null && !nom.isEmpty()) {
                restaurant.getContacts().add(new ContactRestaurantModel(nom, tel, restaurant));
            }
            i++;
        }

        restaurant = restaurantRepository.save(restaurant);

        if (user != null) {
            user.setRole(roleService.getAdmin());
            user.setRestaurant(restaurant);
            userRepository.save(user);
        }

        this.notifierErp(restaurant.getNomEtablissement());
        return ResponseEntity.ok(Map.of("restaurant", restaurant));
    }

    @Transactional
    public Object updateRestaurantV2(
            UpdateRestaurantV2Form form,
            MultipartFile logo,
            MultipartFile coverImage,
            MultipartFile[] pictures,
            MultipartFile document,
            Map<String, String> allParams) {

        UserModel userAuth = genericService.getAuthUser();

        RestaurantModel restaurant;
        if (userAuth != null && userAuth.getRestaurant() != null) {
            // utilisateur connecté : on prend son restaurant
            restaurant = userAuth.getRestaurant();
        } else if (form.getRestoId() != null) {
            // endpoint public : on recherche par restoId
            restaurant = restaurantRepository.findFirstByIdAndDeleted(form.getRestoId(), DeletionEnum.NO)
                    .orElseThrow(() -> new ErreurException("Restaurant introuvable !"));
        } else {
            throw new ErreurException("Vous devez fournir un restoId ou être connecté !");
        }

        // Champs texte
        if (form.getNomEtablissement() != null && !form.getNomEtablissement().isEmpty()) {
            restaurant.setNomEtablissement(form.getNomEtablissement());
        }
        if (form.getTelephone() != null && !form.getTelephone().isEmpty()) {
            restaurant.setTelephone(form.getTelephone());
        }
        if (form.getEmail() != null && !form.getEmail().isEmpty()) {
            restaurant.setEmail(form.getEmail());
        }
        if (form.getLocalisation() != null && !form.getLocalisation().isEmpty()) {
            restaurant.setLocalisation(form.getLocalisation());
        }
        if (form.getCommune() != null && !form.getCommune().isEmpty()) {
            restaurant.setCommune(form.getCommune());
        }
        if (form.getCodePostal() != null && !form.getCodePostal().isEmpty()) {
            restaurant.setCodePostal(form.getCodePostal());
        }
        if (form.getSiteWeb() != null && !form.getSiteWeb().isEmpty()) {
            restaurant.setSiteWeb(form.getSiteWeb());
        }
        if (form.getDescription() != null && !form.getDescription().isEmpty()) {
            restaurant.setDescription(form.getDescription());
        }
        if (form.getDocumentType() != null && !form.getDocumentType().isEmpty()) {
            restaurant.setDocumentType(form.getDocumentType());
        }

        // Commission
        if (form.getTypeCommission() != null && !form.getTypeCommission().isEmpty()) {
            try {
                restaurant.setTypeCommission(TypeCommission.valueOf(form.getTypeCommission().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ErreurException("Type de commission invalide : FIXE ou POURCENTAGE attendu !");
            }
        }
        if (form.getCommission() != null) {
            restaurant.setCommission(form.getCommission());
        }
        if (form.getMethodRecouvrement() != null && !form.getMethodRecouvrement().isEmpty()) {
            restaurant.setMethodRecouvrement(form.getMethodRecouvrement());
        }

        // Logo
        if (logo != null && !logo.isEmpty()) {
            String logExtension = genericService.getFileExtension(logo.getOriginalFilename());
            if (!logExtension.equalsIgnoreCase("png") &&
                    !logExtension.equalsIgnoreCase("jpg") &&
                    !logExtension.equalsIgnoreCase("jpeg")) {
                throw new ErreurException("Le logo doit être au format jpg, jpeg ou png !");
            }
            String logoPath = genericService.generateFileName("logo") + "." + logExtension;
            try {
                logo.transferTo(new File(logoPath).toPath());
                restaurant.setLogo(logoPath);
                restaurant.setLogo_Url(logoPath);
            } catch (IOException e) {
                throw new ErreurException("Erreur lors de la sauvegarde du logo : " + e.getMessage());
            }
        }

        // CoverImage
        if (coverImage != null && !coverImage.isEmpty()) {
            String coverExtension = genericService.getFileExtension(coverImage.getOriginalFilename());
            if (!coverExtension.equalsIgnoreCase("png") &&
                    !coverExtension.equalsIgnoreCase("jpg") &&
                    !coverExtension.equalsIgnoreCase("jpeg")) {
                throw new ErreurException("L'image de couverture doit être au format jpg, jpeg ou png !");
            }
            String coverPath = genericService.generateFileName("cover") + "." + coverExtension;
            try {
                coverImage.transferTo(new File(coverPath).toPath());
                restaurant.setCoverImage(coverPath);
                restaurant.setCoverImageUrl(coverPath);
            } catch (IOException e) {
                throw new ErreurException("Erreur lors de la sauvegarde de l'image de couverture : " + e.getMessage());
            }
        }

        // Document
        if (document != null && !document.isEmpty()) {
            String docExtension = genericService.getFileExtension(document.getOriginalFilename());
            String docPath = genericService.generateFileName("document") + "." + docExtension;
            try {
                document.transferTo(new File(docPath).toPath());
                restaurant.setDocumentUrl(docPath);
            } catch (IOException e) {
                throw new ErreurException("Erreur lors de la sauvegarde du document : " + e.getMessage());
            }
        }

        // Nouvelles photos
        if (pictures != null && pictures.length > 0) {
            for (MultipartFile pic : pictures) {
                if (pic == null || pic.isEmpty()) continue;
                String picExtension = genericService.getFileExtension(pic.getOriginalFilename());
                if (!picExtension.equalsIgnoreCase("png") &&
                        !picExtension.equalsIgnoreCase("jpg") &&
                        !picExtension.equalsIgnoreCase("jpeg")) continue;
                String picPath = genericService.generateFileName("picture_restaurant") + "." + picExtension;
                genericService.compressImage(pic, new File(picPath));
                restaurant.getPictures().add(new PictureRestaurantModel(picPath, restaurant));
            }
        }

        // Mise à jour des horaires (upsert par jour)
        List<OpeningHoursModel> parsedHours = parseOpeningHours(allParams, restaurant);
        if (!parsedHours.isEmpty()) {
            for (OpeningHoursModel oh : parsedHours) {
                Optional<OpeningHoursModel> existing = restaurant.getOpeningHours().stream()
                        .filter(e -> e.getDayOfWeek().equalsIgnoreCase(oh.getDayOfWeek()))
                        .findFirst();
                if (existing.isPresent()) {
                    OpeningHoursModel e = existing.get();
                    e.setClosed(oh.getClosed());
                    if (oh.getOpeningTime() != null) e.setOpeningTime(oh.getOpeningTime());
                    if (oh.getClosingTime() != null) e.setClosingTime(oh.getClosingTime());
                } else {
                    restaurant.getOpeningHours().add(oh);
                }
            }
        }

        // Remplacement des contacts si envoyés
        boolean hasContacts = allParams.keySet().stream().anyMatch(k -> k.startsWith("contact_nom_"));
        if (hasContacts) {
            restaurant.getContacts().clear();
            int i = 0;
            while (allParams.containsKey("contact_nom_" + i)) {
                String nom = allParams.get("contact_nom_" + i);
                String tel = allParams.getOrDefault("contact_telephone_" + i, "");
                if (nom != null && !nom.isEmpty()) {
                    restaurant.getContacts().add(new ContactRestaurantModel(nom, tel, restaurant));
                }
                i++;
            }
        }

        restaurant = restaurantRepository.save(restaurant);
        return ResponseEntity.ok(restaurant);
    }
}
