package com.lunionlab.turbo_restaurant.services;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lunionlab.turbo_restaurant.entities.UserModel;
import com.lunionlab.turbo_restaurant.enums.StatusEnum;

@Service
public class UsersDetailService implements UserDetailsService {
    @Autowired
    JwtService jwtService;
    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String token) {
        Map<String, String> jwtData = jwtService.getInfoFromToken(token);

        String audience = jwtData.get("audience");
        String identifier = jwtData.get("identifier");

        UserModel userModel = userService.getUserByUsernameAndStatus(identifier, StatusEnum.DEFAULT_ENABLE);

        if (userModel == null) {
            throw new UsernameNotFoundException("Utilisateur introuvable ou compte désactivé");
        }

        // Déterminer le rôle Spring Security
        String role = userModel.getRole() != null
                ? "ROLE_" + userModel.getRole().getLibelle().toUpperCase()
                : "ROLE_" + audience.toUpperCase(); // fallback à l’audience

        SimpleGrantedAuthority[] roleArr = new SimpleGrantedAuthority[] {
                new SimpleGrantedAuthority(role),
                new SimpleGrantedAuthority("ROLE_" + audience.toUpperCase())
        };

        return new User(userModel.getUsername(), userModel.getPassword(), Arrays.asList(roleArr));
    }

}
