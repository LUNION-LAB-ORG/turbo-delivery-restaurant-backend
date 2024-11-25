package com.lunionlab.turbo_restaurant.repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lunionlab.turbo_restaurant.model.RestaurantModel;

public interface RestaurantRepository extends JpaRepository<RestaurantModel, UUID> {
        Boolean existsByNomEtablissementAndEmailAndDeleted(String nomEtablissement, String email, Boolean deleted);

        Optional<RestaurantModel> findFirstByIdAndDeleted(UUID restoId, Boolean deleted);

        List<RestaurantModel> findAllByStatusNotInAndDeletedOrderByDateCreationDesc(List<Integer> status,
                        Boolean deleted);

        Page<RestaurantModel> findByStatusNotInAndDeletedOrderByDateCreationDesc(List<Integer> status, Boolean deleted,
                        Pageable page);

        Page<RestaurantModel> findByStatusAndDeletedOrderByDateCreationDesc(Integer status, Boolean deleted,
                        Pageable page);

        Optional<RestaurantModel> findFirstByIdAndStatusAndDeleted(UUID restoId, Integer status, Boolean deleted);

        Optional<RestaurantModel> findFirstByIdAndLocalisationAndStatusAndDeleted(UUID restoId, String localisation,
                        Integer status, Boolean deleted);

        Optional<RestaurantModel> findFirstByNomEtablissementContainingIgnoreCaseAndDeleted(String libelle,
                        Boolean deleted);
}
