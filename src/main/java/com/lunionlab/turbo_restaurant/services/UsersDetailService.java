package com.lunionlab.turbo_restaurant.services;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lunionlab.turbo_restaurant.Enums.JwtAudienceEnum;
import com.lunionlab.turbo_restaurant.Enums.StatusEnum;
import com.lunionlab.turbo_restaurant.model.UserModel;

@Service
public class UsersDetailService implements UserDetailsService {
    @Autowired
    JwtService jwtService;
    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String token) {
        Map<String, String> jwtData = jwtService.getInfoFromToken(token);

        if (jwtData.get("audience").equalsIgnoreCase(JwtAudienceEnum.USER)) {
            UserModel userModel = userService.getUserByUsernameAndStatus(jwtData.get("identifier"),
                    StatusEnum.DEFAULT_ENABLE);
            if (userModel != null) {
                String role = "ROLE_" + userModel.getRole().getLibelle().toUpperCase();
                SimpleGrantedAuthority[] roleArr = new SimpleGrantedAuthority[] {
                        new SimpleGrantedAuthority(role),
                        new SimpleGrantedAuthority("ROLE_" + JwtAudienceEnum.USER)
                };
                List<SimpleGrantedAuthority> roles = Arrays.asList(roleArr);

                return new User(userModel.getUsername(), userModel.getPassword(), roles);
            }
        }
        throw new UsernameNotFoundException("token invalide ou expiré");
    }

}
