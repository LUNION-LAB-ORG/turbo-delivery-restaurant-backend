package com.lunionlab.turbo_restaurant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.model.UserModel;
import com.lunionlab.turbo_restaurant.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Object getUserByUsername(String username) {
        return userRepository.findFirstByUsernameAndDeleted(username, DeletionEnum.NO).orElse(null);

    }

    public UserModel getUserByUsernameAndStatus(String username, Integer status) {
        return userRepository.findFirstByUsernameAndStatusAndDeleted(username, status, DeletionEnum.NO).orElse(null);
    }
}
