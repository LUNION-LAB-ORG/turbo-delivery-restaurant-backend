package com.lunionlab.turbo_restaurant.responses;

import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lunionlab.turbo_restaurant.entities.AccompagnementModel;
import com.lunionlab.turbo_restaurant.entities.OptionPlatModel;
import com.lunionlab.turbo_restaurant.entities.PlatModel;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerPlatResponse {
    private PlatModel platM;
    private List<AccompagnementModel> accompagnementM;
    private List<OptionPlatModel> optionPlatM;

    public CustomerPlatResponse(PlatModel platM, List<AccompagnementModel> accompagnementM,
            List<OptionPlatModel> optionPlatM) {
        this.platM = platM;
        this.accompagnementM = accompagnementM;
        this.optionPlatM = optionPlatM;
    }

}
