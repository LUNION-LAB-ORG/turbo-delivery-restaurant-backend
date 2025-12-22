package com.lunionlab.turbo_restaurant.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lunionlab.turbo_restaurant.model.CollectionModel;

public interface CollectionRepository extends JpaRepository<CollectionModel, UUID> {
    Optional<CollectionModel> findFirstByIdAndDeleted(UUID id, Boolean deleted);

    Boolean existsByLibelleAndDeleted(String libelle, Boolean deleted);

    Optional<CollectionModel> findFirstByLibelleAndDeleted(String libelle, Boolean deleted);

    List<CollectionModel> findAll();

    @Query("""
    SELECT p FROM PlatModel p
    WHERE LOWER(p.libelle) LIKE %:query%
            OR LOWER(p.description) LIKE %:query%
    """)
    List<CollectionModel> searchByLibelleOrDescription(@Param("query") String query);
}
