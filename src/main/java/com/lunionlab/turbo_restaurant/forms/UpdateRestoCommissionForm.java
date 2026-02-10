package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import java.util.UUID;

import com.lunionlab.turbo_restaurant.enums.TypeCommission;

import jakarta.validation.constraints.NotNull;

/**
 * @author mamadou.diarra 2025-02-20
 */
@Data
public class UpdateRestoCommissionForm {
    @NotNull
    private UUID restoId;
    @NotNull
    private TypeCommission type;
    private double commission;
    private String methodRecouvrement;
}
