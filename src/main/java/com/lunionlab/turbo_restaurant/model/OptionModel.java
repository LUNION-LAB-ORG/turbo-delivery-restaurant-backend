package com.lunionlab.turbo_restaurant.model;

import com.lunionlab.turbo_restaurant.Enums.OptionRequiredEnum;
import com.lunionlab.turbo_restaurant.objetvaleur.OptionValeurs;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "options")
@NoArgsConstructor
@Getter
@Setter
public class OptionModel extends BaseModel {

  private String libelle;
  private Boolean isRequired = OptionRequiredEnum.YES;
  private Integer maxSelection = 1;
  @ManyToOne
  private RestaurantModel restaurant;
  @JdbcTypeCode(SqlTypes.JSON)
  private List<OptionValeurs> optionValeurs = new ArrayList<>();

  public OptionModel(String libelle, Boolean isRequired, Integer maxSelection,
      RestaurantModel restaurant) {
    this.libelle = libelle;
    this.isRequired = isRequired;
    this.maxSelection = maxSelection;
    this.restaurant = restaurant;
  }

}
