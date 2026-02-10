package com.lunionlab.turbo_restaurant.entities;

import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Data
@Entity
@NoArgsConstructor
@Table(name = "codeopt")
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
