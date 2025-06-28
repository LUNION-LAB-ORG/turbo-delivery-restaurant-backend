//package com.lunionlab.turbo_restaurant.form;
//
//import com.lunionlab.turbo_restaurant.form.AddPlatForm.OptionPlatCommande;
//import com.lunionlab.turbo_restaurant.form.AddPlatForm.OptionPlatCommande.OptionValeurCommande;
//import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.NotNull;
//import java.util.List;
//import lombok.Getter;
//import lombok.Setter;
//import java.util.UUID;
//
//@Getter
//@Setter
//public class AddOptionPlatForm {
//    @NotEmpty
//    private String libelle;
//    @NotNull
//    private Boolean isRequired;
//    @NotNull
//    private Integer maxSeleteted;
//    @NotNull
//    private UUID platId;
//    @Setter
//    @Getter
//    public static class OptionPlatCommande{
//        @NotNull(message = "Le libelle de l'option est requis !")
//        private String libelle;
//        private Boolean isRequired;
//        @NotNull(message = "La quantité est requis !")
//        private Integer maxSeleteted;
//        private List<AddPlatForm.OptionPlatCommande.OptionValeurCommande> optionValeurCommandes;
//
//        @Setter
//        @Getter
//        public static class OptionValeurCommande{
//            private String valeur;
//            private Long prixSup;
//        }
//
//    }
//}

package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddOptionPlatForm {

  @NotNull
  private UUID platId;
  @NotEmpty
  private List<OptionPlatCommande> optionPlatCommandes;

  @Setter
  @Getter
  public static class OptionPlatCommande {

    @NotNull(message = "Le libelle de l'option est requis !")
    private String libelle;
    private Boolean isRequired;
    @NotNull(message = "La quantité est requis !")
    private Integer maxSeleteted;
    private List<OptionValeurCommande> optionValeurCommandes;

    @Setter
    @Getter
    public static class OptionValeurCommande {

      private String valeur;
      private Long prixSup;
    }

  }
}

