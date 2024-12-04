package com.kdg.hexa_delivery.domain.user;

import com.kdg.hexa_delivery.domain.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "user")
@Getter
public class User extends BaseEntity {

}
