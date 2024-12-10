package com.lunionlab.turbo_restaurant.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.Enums.RoleEnum;
import com.lunionlab.turbo_restaurant.model.RoleModel;
import com.lunionlab.turbo_restaurant.repository.RoleRepository;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public void RoleSeeder() {
        // init valeur
        RoleModel roleAdmin = new RoleModel(RoleEnum.ROLE_ADMIN);
        RoleModel roleManager = new RoleModel(RoleEnum.ROLE_MANAGER);
        RoleModel roleOwner = new RoleModel(RoleEnum.ROLE_OWNER);
        RoleModel roleCaisse = new RoleModel(RoleEnum.ROLE_CAISSE);
        RoleModel roleCompta = new RoleModel(RoleEnum.ROLE_COMPTABLE);
        RoleModel roleRespoLog = new RoleModel(RoleEnum.ROLE_RESPO_LOG);
        // end

        // save
        roleRepository.save(roleAdmin);
        roleRepository.save(roleManager);
        roleRepository.save(roleOwner);
        roleRepository.save(roleCaisse);
        roleRepository.save(roleCompta);
        roleRepository.save(roleRespoLog);
    }

    public RoleModel get(String libelle) {
        return roleRepository.findFirstByLibelleAndDeleted(libelle, DeletionEnum.NO).orElse(null);
    }

    public RoleModel getAdmin() {
        return this.get(RoleEnum.ROLE_ADMIN);
    }

    public RoleModel getManager() {
        return this.get(RoleEnum.ROLE_MANAGER);
    }

    public RoleModel getOwner() {
        return this.get(RoleEnum.ROLE_OWNER);
    }

    public RoleModel getCaisse() {
        return this.get(RoleEnum.ROLE_CAISSE);
    }

    public RoleModel getCompta() {
        return this.get(RoleEnum.ROLE_COMPTABLE);
    }

    public RoleModel getRespoLog() {
        return this.get(RoleEnum.ROLE_RESPO_LOG);
    }

    public Object getRoles() {
        List<RoleModel> roles = roleRepository.findAllByDeleted(DeletionEnum.NO);
        return ResponseEntity.ok(roles);
    }

    public RoleModel getRoleById(UUID roleId) {
        return roleRepository.findFirstByIdAndDeletedFalse(roleId).orElse(null);
    }
}
