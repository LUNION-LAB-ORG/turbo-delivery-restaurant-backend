package com.lunionlab.turbo_restaurant.entities;

import lombok.Data;
import java.util.Date;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "utilisateur")
@NoArgsConstructor
public class UserModel extends BaseModel {
    private String firstName;
    private String lastName;
    private String email;
    private String telephone;
    private String username;
    private String avatar;
    private String avatarUrl;
    private Integer attempt;
    @Column(name = "apikey", unique = true, length = 255)
    private String apiKey;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredPassword;
    private Boolean changePassword;
    @JsonIgnore
    private String password;
    @ManyToOne
    private RoleModel role;
    @ManyToOne
    private RestaurantModel restaurant;

    public UserModel(String firstName, String lastName, String email, String telephone, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telephone = telephone;
        this.username = username;
    }

}
