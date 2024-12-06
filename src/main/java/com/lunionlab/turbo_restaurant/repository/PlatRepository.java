package com.lunionlab.turbo_restaurant.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lunionlab.turbo_restaurant.model.CollectionModel;
import com.lunionlab.turbo_restaurant.model.PlatModel;
import com.lunionlab.turbo_restaurant.model.RestaurantModel;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface PlatRepository extends JpaRepository<PlatModel, UUID> {
        Optional<PlatModel> findFirstByIdAndDeletedAndDisponibleTrue(UUID id, Boolean deleted);

        Optional<PlatModel> findFirstByIdAndRestaurantAndDeletedAndDisponibleTrue(UUID id, RestaurantModel restaurant,
                        Boolean deleted);

        List<PlatModel> findByRestaurantAndCollectionAndPriceGreaterThanEqualAndPriceLessThanEqualAndDeletedAndDisponibleTrue(
                        RestaurantModel resto, CollectionModel collection, Long priceStart, Long priceEnd,
                        Boolean deleted);

        List<PlatModel> findByRestaurantAndPriceGreaterThanEqualAndPriceLessThanEqualAndDeletedAndDisponibleTrue(
                        RestaurantModel resto, Long priceStart, Long priceEnd, Boolean deleted);

        List<PlatModel> findByRestaurantAndLibelleContainingIgnoreCaseAndDisponibleTrueAndDeletedFalse(
                        RestaurantModel resto, String libelle);

        @Query("SELECT DISTINCT p.price FROM PlatModel p ORDER BY p.price ASC")
        List<Long> findAllPriceAsc();

        Page<PlatModel> findByDeletedFalseAndDisponibleTrue(Pageable page);

        Page<PlatModel> findByRestaurantAndDeletedFalseAndDisponibleTrue(RestaurantModel restaurant, Pageable page);

        List<PlatModel> findByRestaurant(RestaurantModel restaurant);
}
