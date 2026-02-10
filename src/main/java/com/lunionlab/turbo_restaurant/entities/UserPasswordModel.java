package com.lunionlab.turbo_restaurant.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user_password")
public class UserPasswordModel extends BaseModel {
    private String password;
    @ManyToOne
    private UserModel user;

    public UserPasswordModel(String password, UserModel user) {
        this.password = password;
        this.user = user;
    }

}
