package com.lunionlab.turbo_restaurant.model;

import java.util.Date;
import java.util.UUID;

import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.Enums.StatusEnum;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Integer status;
    private Boolean deleted;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEdition;

    protected BaseModel(Integer status) {
        this.status = StatusEnum.DEFAULT_ENABLE;
        this.dateCreation = new Date();
        this.dateEdition = new Date();
        this.deleted = DeletionEnum.NO;
    }

    protected BaseModel() {
        this(1);
    }
}
