package com.lunionlab.turbo_restaurant.entities;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "boisson_plat")
@NoArgsConstructor
public class BoissonPlatModel extends BaseModel {
    @ManyToOne
    private PlatModel plat;
    @ManyToOne
    private BoissonModel boissonModel;

    public BoissonPlatModel(PlatModel plat, BoissonModel boissonModel) {
        this.plat = plat;
        this.boissonModel = boissonModel;
    }
}
