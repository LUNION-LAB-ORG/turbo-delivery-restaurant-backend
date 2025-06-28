package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

//@Getter
//@Setter
//public class CreateAccompagnementForm {
//    @NotEmpty
//    private String libelle;
//    @NotNull
//    private Long price;
//    @NotNull
//    private UUID platId;
//    private boolean free;
//    @Setter
//    @Getter
//    public static  class AccompagnementCommande{
//        @NotNull(message = "Le libelle de l'accompagnement est requis !")
//        private String libelle;
//        @NotNull(message = "Le prix de l'accompagnement est requis !")
//        private Long price;
//        private boolean free;
//    }
//}
@Getter
@Setter
public class CreateAccompagnementForm {
  @NotNull(message = "L'identifiant du plat est requis !")
  private UUID platId;
  @NotEmpty(message = "La liste des accompagnement ne doit pas être vide !")
  List<AccompagnementItemForm> accompagnementItemForms;

  @Setter
  @Getter
  public static class AccompagnementItemForm {

    @NotNull(message = "Le libelle de l'accompagnement est requis !")
    private String libelle;
    @NotNull(message = "Le prix de l'accompagnement est requis !")
    private Long price;
    private boolean free;
  }
}
