package com.lunionlab.turbo_restaurant.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accompagnement_plat")
@NoArgsConstructor
@Getter
@Setter
public class AccompagnementPlatModel extends BaseModel {
    @ManyToOne
    private PlatModel platModel;
    @ManyToOne
    private AccompagnementModel accompagnementModel;

    public AccompagnementPlatModel(PlatModel platModel, AccompagnementModel accompagnementModel) {
        this.platModel = platModel;
        this.accompagnementModel = accompagnementModel;
    }
}
