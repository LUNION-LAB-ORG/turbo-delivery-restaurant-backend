package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import java.util.UUID;

@Data
public class RejectRestoForm {
    private String motif;
    private UUID restoId;
}
