package com.lunionlab.turbo_restaurant.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.Enums.TypeCuisineEnum;
import com.lunionlab.turbo_restaurant.model.TypeCuisineModel;
import com.lunionlab.turbo_restaurant.repository.TypeCuisineRepository;

@Service
public class TypeCuisineService {
    @Autowired
    TypeCuisineRepository typeCuisineRepository;

    public void TypeCuisineSeeder() {
        // init valeur
        TypeCuisineModel typeAfrican = new TypeCuisineModel(TypeCuisineEnum.CUISINE_AFRICAINE);
        TypeCuisineModel typeFrancais = new TypeCuisineModel(TypeCuisineEnum.CUISINE_FRANCAISE);
        TypeCuisineModel typeChine = new TypeCuisineModel(TypeCuisineEnum.CUISINE_CHINOISE);
        TypeCuisineModel typeItalian = new TypeCuisineModel(TypeCuisineEnum.CUISINE_ITALIENNE);
        TypeCuisineModel typeLiban = new TypeCuisineModel(TypeCuisineEnum.CUISINE_LIBANAISE);
        TypeCuisineModel typeInter = new TypeCuisineModel(TypeCuisineEnum.CUISINE_INTERNATIONNALE);

        // save
        typeCuisineRepository.save(typeAfrican);
        typeCuisineRepository.save(typeFrancais);
        typeCuisineRepository.save(typeChine);
        typeCuisineRepository.save(typeItalian);
        typeCuisineRepository.save(typeLiban);
        typeCuisineRepository.save(typeInter);
    }

    public Object getTypeCuisine() {
        List<TypeCuisineModel> typeCuisines = typeCuisineRepository.findAllByDeleted(DeletionEnum.NO);
        return ResponseEntity.ok(typeCuisines);
    }

    public TypeCuisineModel get(String libelle) {
        return typeCuisineRepository.findFirstByLibelleAndDeleted(libelle, DeletionEnum.NO).orElse(null);
    }

    public TypeCuisineModel getAfrican() {
        return this.get(TypeCuisineEnum.CUISINE_AFRICAINE);
    }

    public TypeCuisineModel getFran() {
        return this.get(TypeCuisineEnum.CUISINE_FRANCAISE);
    }

    public TypeCuisineModel getLiban() {
        return this.get(TypeCuisineEnum.CUISINE_LIBANAISE);
    }

    public TypeCuisineModel getChina() {
        return this.get(TypeCuisineEnum.CUISINE_CHINOISE);
    }

    public TypeCuisineModel getItalian() {
        return this.get(TypeCuisineEnum.CUISINE_ITALIENNE);
    }

    public TypeCuisineModel getInter() {
        return this.get(TypeCuisineEnum.CUISINE_INTERNATIONNALE);
    }
}
