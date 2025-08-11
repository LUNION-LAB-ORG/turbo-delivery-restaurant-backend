package com.lunionlab.turbo_restaurant.services;

import com.lunionlab.turbo_restaurant.Enums.DayOfWeekEnum;
import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.Enums.StatusEnum;
import com.lunionlab.turbo_restaurant.exception.ErreurException;
import com.lunionlab.turbo_restaurant.exception.ObjetNonTrouveException;
import com.lunionlab.turbo_restaurant.form.*;
import com.lunionlab.turbo_restaurant.model.*;
import com.lunionlab.turbo_restaurant.objetvaleur.TypeCommission;
import com.lunionlab.turbo_restaurant.repository.*;
import com.lunionlab.turbo_restaurant.response.OrderItemResponse;
import com.lunionlab.turbo_restaurant.utilities.Utility;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

@Service
@Slf4j
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final BoissonRespository boissonRespository;
    private final AccompagnementRepo accompagnementRepo;
    private final OptionValeurRepo optionValeurRepo;
    private final UserRepository userRepository;
    private final PictureRestoRepository pictureRestoRepository;
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
        this.pictureRestoRepository = pictureRestoRepository;
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
            log.error("this use hasn't a restaurant");
            throw new ErreurException("Vous n'avez pas de commande pour le moment !");
        }
        Page<UserOrderM> userOrderPage = userOrderRepo.findByRestaurantAndDeletedFalse(restaurant,
                genericService.pagination(0));
        if (userOrderPage.getContent().isEmpty()) {
            log.error("any user order found");
            throw new ErreurException("Vous n'avez pas de commande pour le moment !");
        }

        PagedModel<EntityModel<UserOrderM>> userOrderResource = assembler.toModel(userOrderPage);
        return ResponseEntity.ok(userOrderResource);
    }

    public Object rejectRestaurant(RejectRestoForm form) {
        Optional<RestaurantModel> restOpt = restaurantRepository.findFirstByIdAndDeleted(form.getRestoId(),
                DeletionEnum.NO);
        if (restOpt.isEmpty()) {
            log.error("restaurant not found");
            throw new ErreurException("Le restaurant specifié n'existe pas !");
        }
        RestaurantModel restaurantM = restOpt.get();
        if (restaurantM.getStatus().intValue() != StatusEnum.DEFAULT_DESABLE.intValue()) {
            log.info("reject restaurant");
            restaurantM.setStatus(StatusEnum.DEFAULT_DESABLE);
        } else {
            log.info("ce restaurant a été rejecté");
            throw new ErreurException("Ce restaurant a déjà été rejecté !");
        }

        Boolean isSended = genericService.sendMail("info@turbodeliveryapp.com", restaurantM.getEmail(),
                "Status du compte", genericService.templateReject("Rejet des Infos", "\r\n"
                        + //
                        "                                <div class=\"code\">" + form.getMotif() + "</div>"));
        if (!isSended) {
            log.error("email not sended");
            throw new ErreurException("Mail non distribué !");
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


    private void notifierErp(String nomEtablissement) {
        String message = "Un nouveau restaurant du nom de " + nomEtablissement + " vient d'être créé !";
        String endpoint = BACKEND + "/erp/notification/notifier/erp";
        genericService.httpPost(endpoint, message);
    }
}
