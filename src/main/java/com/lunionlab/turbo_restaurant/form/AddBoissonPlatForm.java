package com.lunionlab.turbo_restaurant.form;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author daniel.kouame 2025-06-27
 */
@Setter
@Getter
public class AddBoissonPlatForm {
    private UUID platId;
    private List<UUID> boissonIds;
}
