package com.lunionlab.turbo_restaurant.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AddPlatForm {
    @NotEmpty
    private String libelle;
    @NotEmpty
    @Size(max = 255, message = "la description doit pas dépasser 255 caractères")
    private String description;
    @NotEmpty
    private String cookTime;
    @NotNull
    private Long price;
    @NotNull
    private UUID collectionId;
//    private List<AccompagnementCommande> accompagnementCommandes;
//    private List<OptionPlatCommande> optionPlatCommandes;
    private List<UUID> boissonIds;
    private MultipartFile imageUrl;
//    @Setter
//    @Getter
//    public static  class AccompagnementCommande{
//        @NotNull(message = "Le libelle de l'accompagnement est requis !")
//        private String libelle;
//        @NotNull(message = "Le prix de l'accompagnement est requis !")
//        private Long price;
//        private boolean free;
//    }
//
//    @Setter
//    @Getter
//    public static class OptionPlatCommande{
//        @NotNull(message = "Le libelle de l'option est requis !")
//        private String libelle;
//        private Boolean isRequired;
//        @NotNull(message = "La quantité est requis !")
//        private Integer maxSeleteted;
//        private List<OptionValeurCommande> optionValeurCommandes;
//
//        @Setter
//        @Getter
//        public static class OptionValeurCommande{
//            private String valeur;
//            private Long prixSup;
//        }
//
//    }
}
