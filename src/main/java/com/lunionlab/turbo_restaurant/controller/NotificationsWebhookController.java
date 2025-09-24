package com.lunionlab.turbo_restaurant.controller;

import com.lunionlab.turbo_restaurant.dto.NotificationsWebhookDto;
import com.lunionlab.turbo_restaurant.model.NotificationsWebhookModel;
import com.lunionlab.turbo_restaurant.services.NotificationsWebhookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/notifications-webhooks")
public class NotificationsWebhookController {

    private final NotificationsWebhookService notificationsWebhookService;

    public NotificationsWebhookController(NotificationsWebhookService notificationsWebhookService) {
        this.notificationsWebhookService = notificationsWebhookService;
    }

    @GetMapping("/lister")
    public ResponseEntity<List<NotificationsWebhookModel>> lister() {
        return ResponseEntity.ok(notificationsWebhookService.lister());
    }

    @PostMapping("/enregistrer")
    public ResponseEntity<NotificationsWebhookModel> enregistrer(@Valid @RequestBody NotificationsWebhookDto dto) {
        return ResponseEntity.ok(notificationsWebhookService.enregistrer(dto));
    }

    @PutMapping("/modifier/{id}")
    public ResponseEntity<NotificationsWebhookModel> modifier(@PathVariable UUID id, @RequestBody NotificationsWebhookDto dto) {
        return ResponseEntity.ok(notificationsWebhookService.modifier(id, dto));
    }

    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable UUID id) {
        notificationsWebhookService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
