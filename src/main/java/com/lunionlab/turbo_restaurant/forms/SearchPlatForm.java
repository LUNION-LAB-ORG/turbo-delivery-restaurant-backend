package com.lunionlab.turbo_restaurant.forms;

import lombok.Data;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;

@Data
public class SearchPlatForm {
    private UUID collectionId;

    private String address;
    @NotNull
    private Long priceStart;
    @NotNull
    private Long priceEnd;

    private UUID restoId;
}
