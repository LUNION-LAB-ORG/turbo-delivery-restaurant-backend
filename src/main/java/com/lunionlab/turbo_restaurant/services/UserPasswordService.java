package com.lunionlab.turbo_restaurant.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lunionlab.turbo_restaurant.entities.UserModel;
import com.lunionlab.turbo_restaurant.entities.UserPasswordModel;
import com.lunionlab.turbo_restaurant.enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.repositories.UserPasswordRepository;
import com.lunionlab.turbo_restaurant.repositories.UserRepository;
import com.lunionlab.turbo_restaurant.utilities.Utility;

@Service
public class UserPasswordService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserPasswordRepository userPasswordRepository;

    public List<UserPasswordModel> getUserHistoricPassword(String username) {
        Optional<UserModel> user = userRepository.findFirstByUsernameAndDeleted(username, DeletionEnum.NO);
        if (user.isEmpty()) {
            return null;
        }
        List<UserPasswordModel> getUserPasswords = userPasswordRepository
                .findTop5ByUserAndDeletedOrderByDateCreationDesc(user.get(), DeletionEnum.NO);
        if (getUserPasswords == null) {
            return null;
        }

        return getUserPasswords;
    }

    public boolean saveUserPassword(String password, UserModel user) {
        List<UserPasswordModel> userPasswords = this.getUserHistoricPassword(user.getUsername());
        if (userPasswords == null) {
            UserPasswordModel userPasswordModel = new UserPasswordModel(Utility.hashPassword(password), user);
            userPasswordRepository.save(userPasswordModel);
            return true;
        }
        for (UserPasswordModel userPassword : userPasswords) {
            if (Utility.checkPassword(password, userPassword.getPassword())) {
                return false;
            }
        }
        UserPasswordModel userPasswordModel = new UserPasswordModel(Utility.hashPassword(password), user);
        userPasswordRepository.save(userPasswordModel);
        return true;

    }
}
