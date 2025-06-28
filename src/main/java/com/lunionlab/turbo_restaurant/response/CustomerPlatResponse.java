package com.lunionlab.turbo_restaurant.response;

import com.lunionlab.turbo_restaurant.model.BoissonModel;
import com.lunionlab.turbo_restaurant.model.BoissonPlatModel;
import com.lunionlab.turbo_restaurant.model.OptionModel;
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
    private List<OptionModel> optionModel;
    private List<BoissonModel> boissonModels;

    public CustomerPlatResponse(PlatModel platM, List<AccompagnementModel> accompagnementM,
            List<OptionModel> optionModel, List<BoissonModel> boissonModels) {
        this.platM = platM;
        this.accompagnementM = accompagnementM;
        this.optionModel = optionModel;
        this.boissonModels = boissonModels;
    }

}
