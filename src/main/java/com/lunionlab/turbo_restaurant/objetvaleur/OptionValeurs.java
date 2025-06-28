package com.lunionlab.turbo_restaurant.objetvaleur;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author daniel.kouame 2025-06-27
 */

@Setter
@Getter
  public class OptionValeurs {
    private String valeur;
    private Long prixSup;

    public OptionValeurs(String valeur, Long prixSup) {
      this.valeur = valeur;
      this.prixSup = prixSup;
    }

}
