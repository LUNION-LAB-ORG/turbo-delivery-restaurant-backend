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

        List<PlatModel> findByCollectionAndPriceGreaterThanEqualAndPriceLessThanEqualAndDeletedAndDisponibleTrue(
                        CollectionModel collection, Long priceStart, Long priceEnd,
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

        List<PlatModel> findByPriceGreaterThanEqualAndPriceLessThanEqualAndDeletedFalseAndDisponibleTrueAndRestaurant(
                        Long priceStart, Long priceEnd, RestaurantModel restaurant);

        List<PlatModel> findByPriceGreaterThanEqualAndPriceLessThanEqualAndDeletedFalseAndDisponibleTrueAndRestaurantAndCollection(
                        Long priceStart, Long priceEnd, RestaurantModel restaurant, CollectionModel collectionModel);

        List<PlatModel> findByPriceGreaterThanEqualAndPriceLessThanEqualAndDeletedFalseAndDisponibleTrue(
                        Long priceStart, Long priceEnd);

        List<PlatModel> findByCollectionAndRestaurantAndDeletedFalse(CollectionModel collectionModel,
                        RestaurantModel restaurantModel);

        @Query("SELECT DISTINCT p.collection FROM PlatModel p WHERE p.deleted=false  AND p.restaurant= ?1")
        List<CollectionModel> findCollectionHasPlat(RestaurantModel restaurant);

        long countByCollectionAndDeletedFalse(CollectionModel collectionModel);

        List<PlatModel> findByCollectionAndRestaurantAndDeleted(CollectionModel collectionModel,
                        RestaurantModel restaurantM, Boolean deleted);

        List<PlatModel> findByCollectionAndDeletedFalseAndDisponibleTrue(CollectionModel collectionModel);

        List<PlatModel> findByCollectionAndPriceGreaterThanEqualAndPriceLessThanEqualAndDeletedAndDisponibleTrueAndRestaurant(
                        CollectionModel collectionModel, Long priceStart, Long priceEnd, Boolean nO,
                        RestaurantModel restaurantModel);
}
