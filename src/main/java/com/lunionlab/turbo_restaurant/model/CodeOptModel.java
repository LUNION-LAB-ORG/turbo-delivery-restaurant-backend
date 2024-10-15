package com.lunionlab.turbo_restaurant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "codeopt")
@Getter
@Setter
@NoArgsConstructor
public class CodeOptModel extends BaseModel {
    private String code;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expired;
    @ManyToOne
    private UserModel user;

    public CodeOptModel(String code, Date expired, UserModel user) {
        this.code = code;
        this.expired = expired;
        this.user = user;
    }

}
