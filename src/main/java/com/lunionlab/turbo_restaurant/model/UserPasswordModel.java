package com.lunionlab.turbo_restaurant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_password")
@NoArgsConstructor
@Getter
@Setter
public class UserPasswordModel extends BaseModel {
    private String password;
    @ManyToOne
    private UserModel user;

    public UserPasswordModel(String password, UserModel user) {
        this.password = password;
        this.user = user;
    }

}
