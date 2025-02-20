package com.lunionlab.turbo_restaurant.form;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RejectRestoForm {
    private String motif;

    private UUID restoId;
}
