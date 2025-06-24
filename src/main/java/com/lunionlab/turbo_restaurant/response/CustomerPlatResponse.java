package com.lunionlab.turbo_restaurant.response;

import com.lunionlab.turbo_restaurant.model.BoissonPlatModel;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lunionlab.turbo_restaurant.model.AccompagnementModel;
import com.lunionlab.turbo_restaurant.model.OptionPlatModel;
import com.lunionlab.turbo_restaurant.model.PlatModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerPlatResponse {
    private PlatModel platM;
    private List<AccompagnementModel> accompagnementM;
    private List<OptionPlatModel> optionPlatM;
    private List<BoissonPlatModel> boissonPlatModels;

    public CustomerPlatResponse(PlatModel platM, List<AccompagnementModel> accompagnementM,
            List<OptionPlatModel> optionPlatM, List<BoissonPlatModel> boissonPlatModels) {
        this.platM = platM;
        this.accompagnementM = accompagnementM;
        this.optionPlatM = optionPlatM;
        this.boissonPlatModels = boissonPlatModels;
    }

}
