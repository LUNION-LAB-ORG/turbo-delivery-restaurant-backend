package com.lunionlab.turbo_restaurant.form;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchPlatForm {
    private UUID collectionId;
    @NotEmpty
    private String address;
    @NotNull
    private Long priceStart;
    @NotNull
    private Long priceEnd;
    @NotNull
    private UUID restoId;
}
