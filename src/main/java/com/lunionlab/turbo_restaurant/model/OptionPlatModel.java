package com.lunionlab.turbo_restaurant.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "option_plat")
@NoArgsConstructor
@Getter
@Setter
public class OptionPlatModel extends BaseModel {

  @ManyToOne
  private PlatModel plat;

  @ManyToOne
  private OptionModel optionModel;

  public OptionPlatModel(PlatModel plat, OptionModel optionModel) {
    this.optionModel = optionModel;
    this.plat = plat;
  }

}
