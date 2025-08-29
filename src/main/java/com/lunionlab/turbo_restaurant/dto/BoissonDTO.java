package com.lunionlab.turbo_restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoissonDTO {
    private String libelle;
    private Long price;
    private Double volume;
}
