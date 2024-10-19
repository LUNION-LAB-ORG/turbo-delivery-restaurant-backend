package com.lunionlab.turbo_restaurant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "boisson_plat")
@NoArgsConstructor
@Getter
@Setter
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
