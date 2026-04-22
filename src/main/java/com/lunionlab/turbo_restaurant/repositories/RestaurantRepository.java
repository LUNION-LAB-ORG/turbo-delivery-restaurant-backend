package com.lunionlab.turbo_restaurant.repositories;

import com.lunionlab.turbo_restaurant.entities.RestaurantModel;
import com.lunionlab.turbo_restaurant.enums.TypeCommission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<RestaurantModel, UUID> {

    boolean existsByEmail(String email);

    Boolean existsByNomEtablissementAndEmailAndDeleted(String nomEtablissement, String email, Boolean deleted);

    Optional<RestaurantModel> findFirstByIdAndDeleted(UUID restoId, Boolean deleted);

    List<RestaurantModel> findAllByStatusNotInAndDeletedOrderByDateCreationDesc(List<Integer> status,
                                                                                Boolean deleted);

    Page<RestaurantModel> findByStatusNotInAndDeletedOrderByDateCreationDesc(List<Integer> status, Boolean deleted,
                                                                             Pageable page);

    Page<RestaurantModel> findByStatusAndDeletedOrderByDateCreationDesc(Integer status, Boolean deleted,
                                                                        Pageable page);

    List<RestaurantModel> findByStatusAndDeletedOrderByDateCreationDesc(Integer status, Boolean deleted);

    Optional<RestaurantModel> findFirstByIdAndStatusAndDeleted(UUID restoId, Integer status, Boolean deleted);

    Optional<RestaurantModel> findFirstByIdAndStatusInAndDeleted(UUID restoId, List<Integer> status,
                                                                 Boolean deleted);

    Optional<RestaurantModel> findFirstByNomEtablissementContainingIgnoreCaseAndDeleted(String libelle,
                                                                                        Boolean deleted);

    Optional<RestaurantModel> findFirstByIdAndStatusAndDeletedFalse(UUID restoId, Integer status);

    Optional<RestaurantModel> findFirstByLocalisationAndStatusAndDeleted(String localisation,
                                                                         Integer status, Boolean deleted);

    Optional<RestaurantModel> findFirstByIdAndLocalisationAndStatusAndDeleted(UUID restoId, String localisation,
                                                                              Integer status, Boolean deleted);
                                                                              
    List<RestaurantModel> findAllByTypeCommissionAndDeletedFalse(TypeCommission typeCommission);

    // Avec pagination
    Page<RestaurantModel> findAllByTypeCommissionAndDeletedFalse(TypeCommission typeCommission, Pageable pageable);

    @Query("""
        SELECT r FROM RestaurantModel r
        WHERE LOWER(r.nomEtablissement) LIKE %:query%
    """)
    List<RestaurantModel> searchByName(@Param("query") String query);

    @Query(
        value = """
            SELECT *
            FROM restaurant r
            WHERE (:nomEtablissement IS NULL OR r.nom_etablissement ILIKE CONCAT('%', :nomEtablissement, '%'))
            AND (:localisation IS NULL OR r.localisation ILIKE CONCAT('%', :localisation, '%'))
            AND (:email IS NULL OR r.email ILIKE CONCAT('%', :email, '%'))
            AND (:telephone IS NULL OR r.telephone ILIKE CONCAT('%', :telephone, '%'))
            AND (:commune IS NULL OR r.commune ILIKE CONCAT('%', :commune, '%'))
            AND (:methodRecouvrement IS NULL OR r.method_recouvrement ILIKE CONCAT('%', :methodRecouvrement, '%'))
            AND (:typeCommission IS NULL OR r.type_commission = :typeCommission)
            AND r.deleted = false
            ORDER BY r.date_service DESC
        """,
        countQuery = """
            SELECT COUNT(*)
            FROM restaurant r
            WHERE (:nomEtablissement IS NULL OR r.nom_etablissement ILIKE CONCAT('%', :nomEtablissement, '%'))
            AND (:localisation IS NULL OR r.localisation ILIKE CONCAT('%', :localisation, '%'))
            AND (:email IS NULL OR r.email ILIKE CONCAT('%', :email, '%'))
            AND (:telephone IS NULL OR r.telephone ILIKE CONCAT('%', :telephone, '%'))
            AND (:commune IS NULL OR r.commune ILIKE CONCAT('%', :commune, '%'))
            AND (:methodRecouvrement IS NULL OR r.method_recouvrement ILIKE CONCAT('%', :methodRecouvrement, '%'))
            AND (:typeCommission IS NULL OR r.type_commission = :typeCommission)
            AND r.deleted = false
        """,
        nativeQuery = true
    )
    Page<RestaurantModel> findWithFilters(
        @Param("nomEtablissement") String nomEtablissement,
        @Param("localisation") String localisation,
        @Param("email") String email,
        @Param("telephone") String telephone,
        @Param("commune") String commune,
        @Param("methodRecouvrement") String methodRecouvrement,
        @Param("typeCommission") String typeCommission,
        Pageable pageable
    );
}
