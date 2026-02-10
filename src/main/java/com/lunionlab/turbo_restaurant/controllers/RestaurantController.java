package com.lunionlab.turbo_restaurant.controllers;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lunionlab.turbo_restaurant.entities.RestaurantModel;
import com.lunionlab.turbo_restaurant.enums.TypeCommission;
import com.lunionlab.turbo_restaurant.forms.AddOpeningForm;
import com.lunionlab.turbo_restaurant.forms.CreateRestaurantForm;
import com.lunionlab.turbo_restaurant.forms.RejectRestoForm;
import com.lunionlab.turbo_restaurant.forms.SearchRestoForm;
import com.lunionlab.turbo_restaurant.forms.UpdateRestaurant;
import com.lunionlab.turbo_restaurant.forms.UpdateRestoCommissionForm;
import com.lunionlab.turbo_restaurant.forms.UserOrderForm;
import com.lunionlab.turbo_restaurant.services.RestaurantService;
import com.lunionlab.turbo_restaurant.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/V1/turbo/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final UserService userService;

    public RestaurantController(RestaurantService restaurantService, UserService userService) {
        this.restaurantService = restaurantService;
        this.userService = userService;
    }

    @Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_USER" })
    @PostMapping("/create")  
    public Object createRestaurant(
            @PathVariable MultipartFile logoUrl,
            @PathVariable MultipartFile cniUrl,
            @PathVariable MultipartFile docUrl,
            @Valid CreateRestaurantForm form
    ) {
        return restaurantService.createRestaurant(logoUrl, cniUrl, docUrl, form);
    }

    @Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_USER" })
    @PostMapping("/update")
    public Object updateRestaurant(
            @PathVariable(required = false) MultipartFile logoUrl,
            @PathVariable(required = false) MultipartFile cniUrl,
            @PathVariable(required = false) MultipartFile docUrl,
            @RequestBody UpdateRestaurant form
    ) {
        return restaurantService.updateRestaurant(logoUrl, cniUrl, docUrl, form);
    }

    @Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_USER" })
    @GetMapping("/info")
    public Object getUserRestaurant() {
        return restaurantService.getUserAuthRestaurant();
    }

    @GetMapping("/not/validated/{page}")
    public Object getAllRestaurantNotValidated(@PathVariable Integer page) {
        return restaurantService.getAllRestaurantNotValidated(page);
    }

    @GetMapping("/validated/authservice/{page}")
    public Object getAllRestaurantValidByAuthService(@PathVariable Integer page) {
        return restaurantService.getAllRestaurantValidByAuthService(page);
    }

    @GetMapping("/validated/opsmanager/{page}")
    public Object getAllRestaurantValidByOpsManager(@PathVariable Integer page) {
        return restaurantService.getAllRestaurantValidByOpsManager(page);
    }

    @GetMapping("/validated/opsmanager")
    public Object getAllRestaurantValidByOpsManager() {
        return restaurantService.getAllRestaurantValidByOpsManager();
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<RestaurantModel>> listRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String nomEtablissement
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("date_service").descending());
        Page<RestaurantModel> restaurants = restaurantService.listRestaurants(nomEtablissement, pageable);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<?> getRestaurantById(@PathVariable UUID id) {
        Optional<RestaurantModel> restaurant = restaurantService.getRestaurantById(id);

        if (restaurant.isPresent()) {
            return ResponseEntity.ok(restaurant.get()); // 200 OK avec body
        } else {
            // 404 avec message simple
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Restaurant non trouvé avec l'id : " + id));
        }
    }

    @GetMapping("/approved/authservice/{restoId}")
    public Object restaurantValidatedByAuthService(@PathVariable UUID restoId) {
        return restaurantService.restaurantValidatedByAuthService(restoId);
    }

    @GetMapping("/approved/opsmanager/{restoId}")
    public Object restaurantValidatedByOpsManager(@PathVariable UUID restoId) {
        return restaurantService.restaurantValidatedByOpsManager(restoId);
    }

    @GetMapping("/detail/erp/{restoId}")
    public Object restaurantDetail(@PathVariable UUID restoId) {
        return restaurantService.restaurantDetail(restoId);
    }

    @GetMapping("/optional/erp/{restoId}")
    public Object optionalDetail(@PathVariable UUID restoId) {
        return restaurantService.optionalDetail(restoId);
    }

    @PostMapping("/search")
    public Object searResto(@Valid @RequestBody SearchRestoForm form) {
        return restaurantService.searResto(form);
    }

    // opening hours
    @Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_USER" })
    @PostMapping("/add/horaire")
    public Object addOpeningHour(@Valid @RequestBody AddOpeningForm form) {
        return restaurantService.addOpeningHours(form);
    }

    @GetMapping("/check/opening/{restoId}")
    public ResponseEntity<Boolean> restoIspOpening(@PathVariable UUID restoId) {
        return restaurantService.restoIsOpen(restoId);
    }

    @Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_USER" })
    @GetMapping("/get/hours")
    public Object getOpeningHours() {
        return restaurantService.getOpeningHours();
    }

    // save user orders
    @PostMapping("/save/order")
    public ResponseEntity<Boolean> saveOrder(@RequestBody UserOrderForm form) {
        return restaurantService.saveUserOrder(form);
    }

    @Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_USER" })
    @GetMapping("/get/user/orders")
    public ResponseEntity<?> getUserOrders() {
        return restaurantService.getUserOrders();
    }

    @PostMapping("/reject")
    public Object rejectRestaurant(@RequestBody RejectRestoForm form) {
        return restaurantService.rejectRestaurant(form);
    }

    @PostMapping("/update-commission")
    public void updateRestoCommission(@Valid @RequestBody UpdateRestoCommissionForm form) {
        restaurantService.updateRestoCommission(form);
    }

    @GetMapping("/{restoId}/users")
    public Object usersRestaurant(@PathVariable UUID restoId) {
        return userService.usersRestaurant(restoId);
    }

    @GetMapping("/commission/fixe")
    public Object getRestaurantsFixe() {
        return restaurantService.getRestaurantsByTypeCommission(TypeCommission.FIXE);
    }

    @GetMapping("/commission/pourcentage")
    public Object getRestaurantsPourcentage() {
        return restaurantService.getRestaurantsByTypeCommission(TypeCommission.POURCENTAGE);
    }

    @GetMapping("/commission/fixe/pagination")
    public Object getRestaurantsFixePage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return restaurantService.getRestaurantsByTypeCommission(TypeCommission.FIXE, page, size);
    }

    @GetMapping("/commission/pourcentage/pagination")
    public Object getRestaurantsPourcentagePage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return restaurantService.getRestaurantsByTypeCommission(TypeCommission.POURCENTAGE, page, size);
    }
}
