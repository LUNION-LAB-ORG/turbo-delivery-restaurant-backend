package com.lunionlab.turbo_restaurant.mappers;

import java.util.List;

import com.lunionlab.turbo_restaurant.dto.PlatWithoutRestaurantAndCollectionDTO;
import com.lunionlab.turbo_restaurant.entities.PlatModel;

public class PlatWithoutRestaurantAndCollectionMapper {
    
    public static PlatWithoutRestaurantAndCollectionDTO toDto(PlatModel model) {
        PlatWithoutRestaurantAndCollectionDTO dto = new PlatWithoutRestaurantAndCollectionDTO();
        dto.setId(model.getId());
        dto.setLibelle(model.getLibelle());
        dto.setDescription(model.getDescription());
        dto.setDisponible(model.getDisponible());
        dto.setCookTime(model.getCookTime());
        dto.setPrice(model.getPrice());
        dto.setImageUrl(model.getImageUrl());
        return dto;
    }

    public static List<PlatWithoutRestaurantAndCollectionDTO> toDtoList(List<PlatModel> models) {
        return models.stream().map(PlatWithoutRestaurantAndCollectionMapper::toDto).toList();
    }
}
