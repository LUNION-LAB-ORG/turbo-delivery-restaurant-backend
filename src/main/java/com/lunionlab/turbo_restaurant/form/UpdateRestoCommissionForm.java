package com.lunionlab.turbo_restaurant.form;

import com.lunionlab.turbo_restaurant.objetvaleur.TypeCommission;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author mamadou.diarra 2025-02-20
 */
@Getter
@Setter
public class UpdateRestoCommissionForm {

    @NotNull
    private UUID restoId;
    @NotNull
    private TypeCommission type;
    private double commission;
}
